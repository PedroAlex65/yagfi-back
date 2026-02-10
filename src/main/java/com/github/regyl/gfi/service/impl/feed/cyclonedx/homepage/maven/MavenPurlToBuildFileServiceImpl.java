package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.maven;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.PurlToBuildFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * Downloads POM from Maven Central for a given Maven artifact PURL.
 * 
 * <p>Builds Maven Central URL by converting groupId dots to path separators.
 * Maven Central uses directory structure: groupId (dots -> slashes) / artifactId / version / artifactId-version.pom
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MavenPurlToBuildFileServiceImpl implements PurlToBuildFileService {

    private static final String MAVEN_CENTRAL_BASE_URL = "https://repo1.maven.org/maven2";

    private final RestClient restClient;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("maven");
    }

    /**
     * Downloads POM from Maven Central.
     * Returns null if artifact doesn't exist (404) or download fails.
     * 
     * @param purl Maven artifact PURL
     * @return POM content as string or null
     */
    @Override
    public String apply(PackageURL purl) {
        String groupId = purl.getNamespace();
        String artifactId = purl.getName();
        String version = purl.getVersion();

        String buildFileUrl = buildBuildFileUrl(groupId, artifactId, version);
        return downloadBuildFile(buildFileUrl);
    }

    private String buildBuildFileUrl(String group, String artifact, String version) {
        String groupPath = group.replace(".", "/");
        return String.format("%s/%s/%s/%s/%s-%s.pom",
                MAVEN_CENTRAL_BASE_URL, groupPath, artifact, version, artifact, version);
    }

    private String downloadBuildFile(String pomUrl) {
        try {
            return restClient.get()
                    .uri(pomUrl)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.debug("Not found pom.xml by url {}", pomUrl);
            return null;
        } catch (Exception e) {
            log.warn("Failed to fetch POM from Maven Central: {}", pomUrl, e);
            return null;
        }
    }
}

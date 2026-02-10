package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.go;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import org.springframework.stereotype.Component;

/**
 * Resolves Go module PURLs to GitHub repository URLs.
 * 
 * <p>Only handles Go packages hosted on GitHub (namespace starts with "github.com").
 * Other Go modules (e.g., from gitlab.com) are not processed as we only track
 * GitHub repositories for issue matching.
 * 
 * <p>Example: pkg:golang/github.com/actions/checkout@v4
 */
@Component
public class GoPurlToHomepageServiceImpl implements PurlToHomepageService {

    @Override
    public boolean test(PackageURL purl) {
        return "golang".equals(purl.getType()) && purl.getNamespace() != null
                && purl.getNamespace().startsWith("github.com");
    }

    /**
     * Constructs GitHub repository URL from Go module PURL.
     * Format: https://{namespace}/{name}
     * 
     * @param purl Go module PURL with GitHub namespace
     * @return GitHub repository URL
     */
    @Override
    public String apply(PackageURL purl) {
        return String.format("https://%s/%s", purl.getNamespace(), purl.getName());
    }
}

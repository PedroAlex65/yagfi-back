package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.github;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import org.springframework.stereotype.Component;

/**
 * Resolves GitHub package PURLs to GitHub repository URLs.
 * Direct mapping: no external API calls needed.
 * 
 * <p>Example: pkg:github/actions/checkout@v4
 */
@Component
public class GithubPurlToHomepageServiceImpl implements PurlToHomepageService {

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("github");
    }

    /**
     * Constructs GitHub repository URL from GitHub package PURL.
     * Format: https://github.com/{namespace}/{name}
     * 
     * @param purl GitHub package PURL
     * @return GitHub repository URL
     */
    @Override
    public String apply(PackageURL purl) {
        return String.format("https://github.com/%s/%s", purl.getNamespace(), purl.getName());
    }
}

package com.github.regyl.gfi.service.feed;

import com.github.packageurl.PackageURL;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Service for resolving PackageURL to GitHub repository homepage URL.
 * Uses Predicate to determine if this service can handle a given PURL type,
 * and Function to convert PURL to repository URL.
 * Multiple implementations exist for different package types (npm, maven, cargo, etc.).
 */
public interface PurlToHomepageService extends Predicate<PackageURL>, Function<PackageURL, String> {
}

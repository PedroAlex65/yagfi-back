package com.github.regyl.gfi.service.feed;

import com.github.packageurl.PackageURL;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Service for downloading build files (package.json, pom.xml, etc.) from package registries.
 * Build files are required to extract repository URLs for packages that don't provide
 * homepage information directly in registry metadata.
 */
public interface PurlToBuildFileService extends Predicate<PackageURL>, Function<PackageURL, String> {
}

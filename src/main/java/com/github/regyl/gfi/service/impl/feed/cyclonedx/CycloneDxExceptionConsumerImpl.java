package com.github.regyl.gfi.service.impl.feed.cyclonedx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Consumer that logs exceptions from CycloneDX SBOM generation failures.
 * Prevents exceptions from being silently swallowed in async processing chains.
 */
@Slf4j
@Component
public class CycloneDxExceptionConsumerImpl implements Consumer<Throwable> {

    @Override
    public void accept(Throwable throwable) {
        String msg = String.format("Error occurred while calling CycloneDxService#accept: %s", throwable.getMessage());
        log.error(msg, throwable);
    }
}

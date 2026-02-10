package com.github.regyl.gfi.service.impl.feed.cyclonedx;

import com.github.regyl.gfi.model.SbomModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Router that delegates SBOM processing results to appropriate consumers.
 * Routes successful results to model consumer and failures to exception consumer.
 * Used as CompletableFuture.whenComplete callback to handle both success and error cases.
 */
@Component
@RequiredArgsConstructor
public class CycloneDxResultRouterImpl implements BiConsumer<SbomModel, Throwable> {

    private final Consumer<SbomModel> modelConsumer;
    private final Consumer<Throwable> exceptionConsumer;

    @Override
    public void accept(SbomModel model, Throwable throwable) {
        if (throwable != null) {
            exceptionConsumer.accept(throwable);
        } else {
            modelConsumer.accept(model);
        }
    }
}

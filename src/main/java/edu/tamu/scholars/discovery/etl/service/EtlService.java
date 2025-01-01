package edu.tamu.scholars.discovery.etl.service;

import static edu.tamu.scholars.discovery.etl.extract.ExtractorCacheUtility.PROPERTY_CACHE;
import static edu.tamu.scholars.discovery.etl.extract.ExtractorCacheUtility.VALUES_CACHE;
import static java.lang.String.format;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.config.model.EtlConfig;
import edu.tamu.scholars.discovery.etl.DataProcessor;
import edu.tamu.scholars.discovery.etl.extract.DataExtractor;
import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.ConfigurableProcessor;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.repo.DataRepo;
import edu.tamu.scholars.discovery.etl.model.type.DataProcessorType;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;

@Slf4j
@Service
@DependsOn({ "defaultsService" })
public class EtlService implements ApplicationListener<ContextRefreshedEvent> {

    private final DataRepo dataRepo;

    private final EtlConfig config;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public EtlService(
            DataRepo dataRepo,
            EtlConfig config,
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.dataRepo = dataRepo;
        this.config = config;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Instant startTime = Instant.now().plusMillis(10000);
        threadPoolTaskScheduler.schedule(this::process, startTime);
    }

    private <I, O> void process() {
        List<CompletableFuture<EtlContext<I, O>>> futures = dataRepo.findAll()
                .stream()
                .<EtlContext<I, O>>map(this::init)
                .toList()
                .stream()
                .map(this::process)
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .thenAccept(contexts -> {
                    log.info("All ETL processes completed");
                    contexts.stream().forEach(this::destroy);
                    PROPERTY_CACHE.clear();
                    VALUES_CACHE.clear();
                })
                .exceptionally(throwable -> {
                    log.error("Error during ETL processes", throwable);
                    PROPERTY_CACHE.clear();
                    VALUES_CACHE.clear();

                    return null;
                });
    }

    private <I, O> EtlContext<I, O> init(Data data) {
        EtlContext<I, O> context = new EtlContext<>(data, getExtractor(data), getTransformer(data), getLoader(data));

        context.extractor.init();
        context.transformer.init();
        context.loader.init();

        context.extractor.preprocess();
        context.transformer.preprocess();
        context.loader.preprocess();

        return context;
    }

    private <I, O> CompletableFuture<EtlContext<I, O>> process(EtlContext<I, O> context) {
        log.info("Starting ETL {}", context.data.getName());

        CompletableFuture<EtlContext<I, O>> future = new CompletableFuture<>();

        context.extract()
                .map(context::transform)
                .buffer(config.getBufferSize())
                .subscribe(
                        context::load,
                        error -> {
                            String message = format("Error processing ETL for %s", context.data.getName());
                            log.error(message, error);
                            future.completeExceptionally(error);
                        },
                        () -> {
                            log.info("Completed ETL for {}", context.data.getName());
                            future.complete(context);
                        });

        return future;
    }

    private <I, O> void destroy(EtlContext<I, O> context) {
        context.extractor.postprocess();
        context.transformer.postprocess();
        context.loader.postprocess();

        context.extractor.destroy();
        context.transformer.destroy();
        context.loader.destroy();
    }

    @SuppressWarnings("unchecked")
    private <I> DataExtractor<I> getExtractor(Data data) {
        return (DataExtractor<I>) getTypedDataProcessor(data.getExtractor(), data);
    }

    @SuppressWarnings("unchecked")
    private <I, O> DataTransformer<I, O> getTransformer(Data data) {
        return (DataTransformer<I, O>) getTypedDataProcessor(data.getTransformer(), data);
    }

    @SuppressWarnings("unchecked")
    private <O> DataLoader<O> getLoader(Data data) {
        return (DataLoader<O>) getTypedDataProcessor(data.getLoader(), data);
    }

    private <P extends DataProcessor> P getTypedDataProcessor(
            ConfigurableProcessor<P, ? extends DataProcessorType<P>> processor, Data data) {
        return processor.getType().getDataProcessor(data);
    }

    @RequiredArgsConstructor
    private static class EtlContext<I, O> {

        private final Data data;

        private final DataExtractor<I> extractor;

        private final DataTransformer<I, O> transformer;

        private final DataLoader<O> loader;

        public Flux<I> extract() {
            return extractor.extract();
        }

        public O transform(I input) {
            return transformer.transform(input);
        }

        public void load(Collection<O> input) {
            loader.load(input);
        }

    }

}

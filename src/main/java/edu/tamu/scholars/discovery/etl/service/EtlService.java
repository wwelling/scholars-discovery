package edu.tamu.scholars.discovery.etl.service;

import static java.lang.String.format;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.component.Destination;
import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.component.Source;
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
@DependsOn({ "defaultsService", "source", "destination", "mapper" })
public class EtlService implements ApplicationListener<ContextRefreshedEvent> {

    private final DataRepo dataRepo;

    private final EtlConfig config;

    private final ApplicationContext applicationContext;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final Map<String, DataExtractor<?>> extractors;
    private final Map<String, DataTransformer<?, ?>> transformers;
    private final Map<String, DataLoader<?>> loaders;

    public EtlService(
            DataRepo dataRepo,
            EtlConfig config,
            ApplicationContext applicationContext,
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.dataRepo = dataRepo;
        this.config = config;
        this.applicationContext = applicationContext;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;

        this.extractors = new ConcurrentHashMap<>();
        this.transformers = new ConcurrentHashMap<>();
        this.loaders = new ConcurrentHashMap<>();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Instant startTime = Instant.now().plusMillis(10000);
        threadPoolTaskScheduler.schedule(this::process, startTime);
    }

    private void process() {
        List<Data> data = new ArrayList<>(dataRepo.findAll());
        // initialization must be done sequentially
        data.forEach(this::init);

        List<CompletableFuture<Void>> futures = data.parallelStream()
            .map(this::process)
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                log.info("All ETL processes completed");
                destroy();
            })
            .exceptionally(throwable -> {
                log.error("Error during ETL processes", throwable);

                return null;
            });
    }

    private <I, O> void init(Data datum) {
        Source<?, ?, ?> source = applicationContext.getBean(datum.getExtractor().getType().getServiceType());
        source.init();

        DataExtractor<I> extractor = getExtractor(datum, source);
        extractor.init();
        extractor.preProcess();
        this.extractors.put(datum.getName(), extractor);

        Mapper<?> mapper = applicationContext.getBean(datum.getTransformer().getType().getServiceType());
        mapper.init();

        DataTransformer<I, O> transformer = getTransformer(datum, mapper);
        transformer.init();
        transformer.preProcess();
        this.transformers.put(datum.getName(), transformer);

        Destination destination = applicationContext.getBean(datum.getLoader().getType().getServiceType());
        destination.init();

        DataLoader<O> loader = getLoader(datum, destination);
        loader.init();
        loader.preProcess();
        this.loaders.put(datum.getName(), loader);
    }

    private <I, O> CompletableFuture<Void> process(Data datum) {
        log.info("Starting ETL {}", datum.getName());

        DataExtractor<I> extractor = getExtractor(datum);
        DataTransformer<I, O> transformer = getTransformer(datum);
        DataLoader<O> loader = getLoader(datum);

        CompletableFuture<Void> future = new CompletableFuture<>();

        extractor.extract()
            .map(transformer::transform)
            .buffer(config.getBufferSize())
            .subscribe(
                loader::load,
                error -> {
                    String message = format("Error processing ETL for %s", datum.getName());
                    log.error(message, error);
                    future.completeExceptionally(error);
                },
                () -> {
                    log.info("Completed ETL for {}", datum.getName());
                    future.complete(null);
                });

        return future;
    }

    private void destroy() {
        this.extractors.values().forEach(DataExtractor::postProcess);
        this.transformers.values().forEach(DataTransformer::postProcess);
        this.loaders.values().forEach(DataLoader::postProcess);

        this.extractors.values().forEach(DataExtractor::destroy);
        this.transformers.values().forEach(DataTransformer::destroy);
        this.loaders.values().forEach(DataLoader::destroy);

        this.extractors.clear();
        this.transformers.clear();
        this.loaders.clear();
    }

    private <P extends DataProcessor, S extends edu.tamu.scholars.discovery.component.Service> P getTypedDataProcessor(
            ConfigurableProcessor<P, S, ? extends DataProcessorType<P, S>> processor, Data datum, S service) {
        return processor.getType().getDataProcessor(datum, service);
    }

    @SuppressWarnings("unchecked")
    private <I> DataExtractor<I> getExtractor(Data datum, Source<?, ?, ?> source) {
        return (DataExtractor<I>) getTypedDataProcessor(datum.getExtractor(), datum, source);
    }

    @SuppressWarnings("unchecked")
    private <I, O> DataTransformer<I, O> getTransformer(Data datum, Mapper<?> mapper) {
        return (DataTransformer<I, O>) getTypedDataProcessor(datum.getTransformer(), datum, mapper);
    }

    @SuppressWarnings("unchecked")
    private <O> DataLoader<O> getLoader(Data datum, Destination desintation) {
        return (DataLoader<O>) getTypedDataProcessor(datum.getLoader(), datum, desintation);
    }

    @SuppressWarnings("unchecked")
    private <I> DataExtractor<I> getExtractor(Data datum) {
        return (DataExtractor<I>) extractors.get(datum.getName());
    }

    @SuppressWarnings("unchecked")
    private <I, O> DataTransformer<I, O> getTransformer(Data datum) {
        return (DataTransformer<I, O>) transformers.get(datum.getName());
    }

    @SuppressWarnings("unchecked")
    private <O> DataLoader<O> getLoader(Data datum) {
        return (DataLoader<O>) loaders.get(datum.getName());
    }

}

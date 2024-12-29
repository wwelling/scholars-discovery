package edu.tamu.scholars.discovery.etl.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.component.Destination;
import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.component.Source;
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

    private final Source<?, ?, ?> source;

    private final Mapper mapper;

    private final Destination destination;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public EtlService(
            DataRepo dataRepo,
            Source<?, ?, ?> source,
            Mapper mapper,
            Destination destination,
            ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.dataRepo = dataRepo;
        this.source = source;
        this.mapper = mapper;
        this.destination = destination;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Instant startTime = Instant.now().plusMillis(10000);
        threadPoolTaskScheduler.schedule(this::process, startTime);
    }

    private void process() {
        List<Data> data = new ArrayList<>(dataRepo.findAll());
        init();
        data.parallelStream().forEach(datum -> {

            log.info("Starting ETL {}", datum.getName());

            getExtractor(datum, source).extract()
                .map(getTransformer(datum, mapper)::transform)
                .buffer(500) // TODO: get from properties
                .subscribe(
                    getLoader(datum, destination)::load,
                    error -> destroy(datum),
                    () -> destroy(datum));
        });
    }

    private void init() {
        source.init();
        mapper.init();
        destination.init();
        // initialize all processors before beginning
        for (Data datum : dataRepo.findAll()) {
            init(datum.getExtractor(), datum, source);
            init(datum.getTransformer(), datum, mapper);
            init(datum.getLoader(), datum, destination);
        }
    }

    private void destroy(Data datum) {
        destroy(datum.getExtractor(), datum, source);
        destroy(datum.getTransformer(), datum, mapper);
        destroy(datum.getLoader(), datum, destination);
        log.info("ETL {} finished", datum.getName());
    }

    private <P extends DataProcessor, S extends edu.tamu.scholars.discovery.component.Service, T extends DataProcessorType<P, S>, C extends ConfigurableProcessor<P, S, T>> void init(
            C processor, Data datum, S service) {
        P dataProcessor = getTypedDataProcessor(processor, datum, service);
        dataProcessor.init();
        dataProcessor.preProcess();
    }

    private <P extends DataProcessor, S extends edu.tamu.scholars.discovery.component.Service, T extends DataProcessorType<P, S>, C extends ConfigurableProcessor<P, S, T>> void destroy(
            C processor, Data datum, S service) {
        P dataProcessor = getTypedDataProcessor(processor, datum, service);
        dataProcessor.postProcess();
        dataProcessor.destroy();
    }

    private <P extends DataProcessor, S extends edu.tamu.scholars.discovery.component.Service> P getTypedDataProcessor(
            ConfigurableProcessor<P, S, ? extends DataProcessorType<P, S>> processor, Data datum, S service) {
        return processor.getType()
                .getDataProcessor(datum, service);
    }

    @SuppressWarnings("unchecked")
    private <I> DataExtractor<I> getExtractor(Data datum, Source<?, ?, ?> source) {
        return (DataExtractor<I>) getTypedDataProcessor(datum.getExtractor(), datum, source);
    }

    @SuppressWarnings("unchecked")
    private <I, O> DataTransformer<I, O> getTransformer(Data datum, Mapper mapper) {
        return (DataTransformer<I, O>) getTypedDataProcessor(datum.getTransformer(), datum, mapper);
    }

    @SuppressWarnings("unchecked")
    private <O> DataLoader<O> getLoader(Data datum, Destination desintation) {
        return (DataLoader<O>) getTypedDataProcessor(datum.getLoader(), datum, desintation);
    }

}

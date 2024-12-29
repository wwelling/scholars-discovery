package edu.tamu.scholars.discovery.etl.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public EtlService(DataRepo dataRepo, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.dataRepo = dataRepo;
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

            getExtractor(datum).extract()
                .map(getTransformer(datum)::transform)
                .buffer(500) // TODO: get from properties
                .subscribe(
                    getLoader(datum)::load,
                    error -> destroy(datum),
                    () -> destroy(datum));
        });
    }

    private void init() {
        // initialize all processors before beginning
        for (Data datum : dataRepo.findAll()) {
            init(datum.getExtractor(), datum);
            init(datum.getTransformer(), datum);
            init(datum.getLoader(), datum);
        }
    }

    private void destroy(Data datum) {
        destroy(datum.getExtractor(), datum);
        destroy(datum.getTransformer(), datum);
        destroy(datum.getLoader(), datum);
        log.info("ETL {} finished", datum.getName());
    }

    private <P extends DataProcessor, T extends DataProcessorType<P>, C extends ConfigurableProcessor<T>> void init(C processor, Data datum) {
        P dataProcessor = getTypedDataProcessor(processor, datum);
        dataProcessor.init();
        dataProcessor.preProcess();
    }

    private <P extends DataProcessor, T extends DataProcessorType<P>, C extends ConfigurableProcessor<T>> void destroy(C processor, Data datum) {
        P dataProcessor = getTypedDataProcessor(processor, datum);
        dataProcessor.postProcess();
        dataProcessor.destroy();
    }

    private <P extends DataProcessor> P getTypedDataProcessor(ConfigurableProcessor<? extends DataProcessorType<P>> processor, Data datum) {
        return processor.getType()
            .getDataProcessor(datum);
    }

    @SuppressWarnings("unchecked")
    private <I> DataExtractor<I> getExtractor(Data datum) {
        return (DataExtractor<I>) getTypedDataProcessor(datum.getExtractor(), datum);
    }

    @SuppressWarnings("unchecked")
    private <I, O> DataTransformer<I, O> getTransformer(Data datum) {
        return (DataTransformer<I, O>) getTypedDataProcessor(datum.getTransformer(), datum);
    }

    @SuppressWarnings("unchecked")
    private <O> DataLoader<O> getLoader(Data datum) {
        return (DataLoader<O>) getTypedDataProcessor(datum.getLoader(), datum);
    }

}

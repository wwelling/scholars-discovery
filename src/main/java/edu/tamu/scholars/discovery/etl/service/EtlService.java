package edu.tamu.scholars.discovery.etl.service;

import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
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

@Service
@DependsOn("defaultsService")
public class EtlService implements ApplicationListener<ContextRefreshedEvent> {

    private final DataRepo dataRepo;

    public EtlService(DataRepo dataRepo) {
        this.dataRepo = dataRepo;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        process();
    }

    private <I, O> void process() {
        init();
        for (Data data : dataRepo.findAll()) {
            EtlContext<I, O> etlContext = createEtlContext(data);

            etlContext.extract()
                .map(etlContext::transform)
                .buffer(500)
                .subscribe(
                    etlContext::load,
                    error -> destroy(data),
                    () -> destroy(data));
        }
    }

    private void init() {
        // initialize all processors before beginning
        for (Data data : dataRepo.findAll()) {
            init(data.getExtractor(), data);
            init(data.getTransformer(), data);
            init(data.getLoader(), data);
        }
    }

    private void destroy(Data data) {
        destroy(data.getExtractor(), data);
        destroy(data.getTransformer(), data);
        destroy(data.getLoader(), data);
    }

    private <P extends DataProcessor, T extends DataProcessorType<P>, C extends ConfigurableProcessor<T>> void init(C processor, Data data) {
        P dataProcessor = getTypedDataProcessor(processor, data);
        dataProcessor.init();
        dataProcessor.preProcess();
    }

    private <P extends DataProcessor, T extends DataProcessorType<P>, C extends ConfigurableProcessor<T>> void destroy(C processor, Data data) {
        P dataProcessor = getTypedDataProcessor(processor, data);
        dataProcessor.postProcess();
        dataProcessor.destroy();
    }

    private <P extends DataProcessor> P getTypedDataProcessor(ConfigurableProcessor<? extends DataProcessorType<P>> processor, Data data) {
        return processor.getType()
            .getDataProcessor(data);
    }

    private <I, O> EtlContext<I, O> createEtlContext(Data data) {
        return new EtlContext<>(getExtractor(data), getTransformer(data), getLoader(data));
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

    @RequiredArgsConstructor
    private static class EtlContext<I, O> {

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

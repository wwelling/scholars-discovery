package edu.tamu.scholars.discovery.etl.service;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.etl.load.DataLoader;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.Loader;
import edu.tamu.scholars.discovery.etl.model.repo.DataRepo;

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

    private void process() {
        init();
        for (Data data : dataRepo.findAll()) {
            System.out.println(data.getName());
        }
        destroy();
    }

    private void init() {
        for (Data data : dataRepo.findAll()) {
            List<DataField> fields = data.getFields();
            Loader loader = data.getLoader();
            DataLoader<?> dataLoader = loader.getType()
                    .getDataProcessor(loader.getAttributes());
            dataLoader.init();
            dataLoader.preProcess(fields);
        }
    }

    private void destroy() {
        for (Data data : dataRepo.findAll()) {
            List<DataField> fields = data.getFields();
            Loader loader = data.getLoader();
            DataLoader<?> dataLoader = loader.getType()
                    .getDataProcessor(loader.getAttributes());
            dataLoader.postProcess(fields);
            dataLoader.destroy();
        }
    }

}

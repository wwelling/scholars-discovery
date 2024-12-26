package edu.tamu.scholars.discovery.etl.service;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.model.DataField;
import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;
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
        for (Data data : dataRepo.findAll()) {

            System.out.println("\n" + data.getName());

            for (DataField field : data.getFields()) {
                initializeDescriptor(field.getDescriptor());

                field.getNestedDescriptors()
                        .forEach(this::initializeDescriptor);
            }
        }

    }

    private void initializeDescriptor(DataFieldDescriptor descriptor) {
        System.out.println("\t" + descriptor.getName());

        FieldDestination destination = descriptor.getDestination();

    }

}

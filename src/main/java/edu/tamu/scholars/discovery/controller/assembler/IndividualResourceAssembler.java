package edu.tamu.scholars.discovery.controller.assembler;

import java.util.List;

import org.springframework.data.util.StreamUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.controller.IndividualSearchController;
import edu.tamu.scholars.discovery.controller.assembler.model.IndividualCollectionModel;
import edu.tamu.scholars.discovery.controller.assembler.model.IndividualModel;
import edu.tamu.scholars.discovery.model.Individual;


@Component
public class IndividualResourceAssembler extends RepresentationModelAssemblerSupport<Individual, IndividualModel> {

    public IndividualResourceAssembler() {
        super(IndividualSearchController.class, IndividualModel.class);
    }

    @Override
    public IndividualModel toModel(Individual document) {
        return new IndividualModel(document, List.of());
    }

    @Override
    public CollectionModel<IndividualModel> toCollectionModel(Iterable<? extends Individual> entities) {
        List<IndividualModel> content = StreamUtils.createStreamFromIterator(entities.iterator())
            .map(this::toModel)
            .toList();

        return new IndividualCollectionModel(content, List.of(), null);
    }

}

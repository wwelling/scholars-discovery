package edu.tamu.scholars.discovery.controller.assembler.model;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import edu.tamu.scholars.discovery.model.Individual;


public class IndividualModel extends EntityModel<Individual> {

    public IndividualModel(Individual individual, Iterable<Link> links) {
        super(individual, links);
    }

}

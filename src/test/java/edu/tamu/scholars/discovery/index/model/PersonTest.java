package edu.tamu.scholars.discovery.index.model;

import edu.tamu.scholars.discovery.index.model.Person;

public class PersonTest extends AbstractIndexDocumentTest<Person> {

    @Override
    protected Class<?> getType() {
        return Person.class;
    }

}

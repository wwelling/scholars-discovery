package edu.tamu.scholars.discovery.component.triplestore;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;

import edu.tamu.scholars.discovery.component.Source;

public interface Triplestore extends Source<Triple, Model, Query> {

    public QueryExecution createQueryExecution(Query query);

    public QueryExecution createQueryExecution(String query);

}

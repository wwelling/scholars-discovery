package edu.tamu.scholars.discovery.etl.extract.jena;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.factory.triplestore.jena.TdbTriplestore;

@Slf4j
public class TdbTriplestoreExtractor extends AbstractTriplestoreExtractor {

    private final TdbTriplestore triplestore;

    public TdbTriplestoreExtractor(Data data) {
        super(data);
        this.triplestore = TdbTriplestore.of(data.getExtractor().getAttributes());
    }

    @Override
    public void destroy() {
        triplestore.close();
    }

    @Override
    protected Iterator<Triple> queryCollection(String query) {
        return triplestore.collection(query);
    }

    @Override
    protected Model queryIndividual(String query) {
        return triplestore.individual(query);
    }

}

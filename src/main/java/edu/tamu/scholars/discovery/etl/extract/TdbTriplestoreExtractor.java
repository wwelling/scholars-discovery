package edu.tamu.scholars.discovery.etl.extract;

import java.util.Iterator;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;

import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.factory.triplestore.TdbTriplestore;

@Slf4j
public class TdbTriplestoreExtractor extends AbstractTriplestoreExtractor {

    private final TdbTriplestore triplestore;

    public TdbTriplestoreExtractor(Data data) {
        super(data);

        Map<String, String> properties = data.getExtractor().getAttributes();

        String directory = properties.getOrDefault("directory", "triplestore");

        this.triplestore = TdbTriplestore.of(directory);
    }

    @Override
    public void destroy() {
        triplestore.close();
        cache.clear();
    }

    @Override
    protected Iterator<Triple> queryCollection(String query) {
        return triplestore.queryCollection(query);
    }

    @Override
    protected Model queryIndividual(String query) {
        return triplestore.queryIndividual(query);
    }

}

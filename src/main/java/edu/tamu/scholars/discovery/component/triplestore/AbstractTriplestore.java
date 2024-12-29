package edu.tamu.scholars.discovery.component.triplestore;

import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;

@Slf4j
public abstract class AbstractTriplestore implements Triplestore {

    @Override
    public Iterator<Triple> queryCollection(Query query) {
        QueryExecution qe = createQueryExecution(query);

        return new Iterator<Triple>() {

            private final Iterator<Triple> innerIterator = qe.execConstructTriples();

            @Override
            public boolean hasNext() {
                boolean hasNext = innerIterator.hasNext();
                if (!hasNext) {
                    qe.close();
                }

                return hasNext;
            }

            @Override
            public Triple next() {
                return innerIterator.next();
            }

        };
    }

    @Override
    public Model queryIndividual(Query query) {
        try (QueryExecution qe = createQueryExecution(query)) {
            return queryIndividual(qe);
        }
    }

    @Override
    public Model queryIndividual(String query) {
        try (QueryExecution qe = createQueryExecution(query)) {
            return queryIndividual(qe);
        }
    }

    private Model queryIndividual(QueryExecution qe) {
        Model model = qe.execConstruct();
        if (log.isDebugEnabled()) {
            model.write(System.out, "RDF/XML"); // NOSONAR
        }

        return model;
    }

}

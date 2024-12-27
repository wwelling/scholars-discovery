package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import edu.tamu.scholars.discovery.service.HttpTriplestore;
import edu.tamu.scholars.discovery.service.TdbTriplestore;
import edu.tamu.scholars.discovery.service.Triplestore;

/**
 * {@link MiddlewareConfig} configuration to specify triplestore.
 * 
 * <p>
 * See `discovery.triplestore` in src/main/resources/application.yml.
 * </p>
 */
@Data
@NoArgsConstructor
public class TriplestoreConfig {

    private Class<? extends Triplestore> type = TdbTriplestore.class;

    /** directory property used by {@link TdbTriplestore } */
    private String directory = "triplestore";

    /** directory property used by {@link HttpTriplestore } */
    private String datasourceUrl;

}

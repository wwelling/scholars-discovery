package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.component.triplestore.TdbTriplestore;
import edu.tamu.scholars.discovery.component.triplestore.Triplestore;

@Data
@NoArgsConstructor
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "discovery.triplestore")
public class TriplestoreConfig extends ComponentConfig<Triplestore> {

    // TODO: add validations and @Validated to the class

    private Class<? extends Triplestore> type = TdbTriplestore.class;

    private String directory = "triplestore";

    private String url = "http://localhost:8080/vivo/api/sparqlQuery";

}

package edu.tamu.scholars.discovery.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "discovery.index")
public class IndexConfig {

    // TODO: add validations and @Validated to the class

    private IndexType type = IndexType.SOLR;

    private String host = "http://localhost:8983/solr";

    private String collection = "scholars-discovery";

    private String operator = "OR";

    private String parser = "edismax";

    private String zone = "America/Chicago";

    private int connectionTimeout = 1;

    private int idleTimeout = 5;

    private int maxConnectionPerHost = 10;

    private int requestTimeout = 5;

    @Getter
    @AllArgsConstructor
    public enum IndexType {
        SOLR("solr");

        private final String value;
    }

}

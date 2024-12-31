package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
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

    private String host = "http://localhost:8983/solr";

    private String operator = "OR";

    private String parser = "edismax";

    private String name = "scholars-discovery";

    private String cron = "0 0 0 * * SUN";

    private String zone = "America/Chicago";

    private boolean schematize = true;

    private boolean onStartup = true;

    private boolean enableIndividualOnBatchFail = true;

    private int onStartupDelay = 10000;

    private int batchSize = 10000;

}

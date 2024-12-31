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
@ConfigurationProperties(prefix = "discovery.triplestore")
public class TriplestoreConfig {

    // TODO: add validations and @Validated to the class

    private String directory = "triplestore";

}

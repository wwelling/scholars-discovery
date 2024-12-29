package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "discovery.export")
public class ExportConfig {

    // TODO: add validations and @Validated to the class

    private String individualKey = "individual";

    private String individualBaseUri = "http://localhost:4200/display";

}

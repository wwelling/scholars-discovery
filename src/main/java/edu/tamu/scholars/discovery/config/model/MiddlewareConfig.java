package edu.tamu.scholars.discovery.config.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.auth.config.AuthConfig;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "discovery")
public class MiddlewareConfig {

    // TODO: add validations and @Validated to the class

    private String assetsLocation = "classpath:assets";

    private boolean loadDefaults = true;

    private boolean updateDefaults = false;

    private List<String> allowedOrigins = List.of("http://localhost:4200");

    private AuthConfig auth = new AuthConfig();

    private MailConfig mail = new MailConfig();

    private HttpConfig http = new HttpConfig();

    private ExportConfig export = new ExportConfig();

    private IndexConfig index = new IndexConfig();

    private MapperConfig mapper = new MapperConfig();

    private TriplestoreConfig triplestore = new TriplestoreConfig();

}

package edu.tamu.scholars.discovery.config.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.component.json.JsonMapper;

@Data
@NoArgsConstructor
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "discovery.mapper")
public class MapperConfig extends ComponentConfig<Mapper> {

    // TODO: add validations and @Validated to the class

    private Class<? extends Mapper> type = JsonMapper.class;

}

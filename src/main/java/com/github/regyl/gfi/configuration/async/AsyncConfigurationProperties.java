package com.github.regyl.gfi.configuration.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.properties.async")
public class AsyncConfigurationProperties {

    private Integer corePoolSize;
    private Integer maxPoolSize;
}

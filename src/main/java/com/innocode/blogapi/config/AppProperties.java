package com.innocode.blogapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("app")
@Configuration
@Data
public class AppProperties {
    private String secretKey;
}

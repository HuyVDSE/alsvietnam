package com.alsvietnam.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "google.firebase")
public class GoogleFirebaseProperties {

    private String bucket;

    private String url;

    private String privateKey;
}

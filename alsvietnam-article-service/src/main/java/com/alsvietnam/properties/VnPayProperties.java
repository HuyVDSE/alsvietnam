package com.alsvietnam.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Duc_Huy
 * Date: 8/24/2022
 * Time: 9:00 AM
 */

@Data
@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "vnpay")
public class VnPayProperties {

    private String apiVersion;

    private String payUrl;

    private String returnUrl;

    private String tmnCode;

    private String hashSecret;

    private String apiUrl;

}

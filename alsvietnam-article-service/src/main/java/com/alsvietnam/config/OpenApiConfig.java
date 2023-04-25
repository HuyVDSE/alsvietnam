package com.alsvietnam.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Duc_Huy
 * Date: 6/27/2022
 * Time: 11:09 PM
 */

@Configuration
public class OpenApiConfig {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public OpenAPI customOpenApi() {
        String securitySchemeName = "bearerAuth";

        List<Server> servers = new ArrayList<>();
        if (!activeProfile.equals("prod")) {
            servers.add(new Server().url("http://localhost:8080"));
        }
        servers.add(new Server().url("https://api.alsvietnam.org"));
        servers.add(new Server().url("http://18.140.244.81:8080"));

        return new OpenAPI()
                .servers(servers)
                .info(new Info().title("ALS Viet Nam Article Service")
                        .description("Open API 3.0")
                        .contact(new Contact()
                                .email("alsvietnam.work@gmail.com")
                                .name("alsvietnam")
                                .url("https://alsvietnam.com"))
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

package io.kontakt.apps.temerature.analytics.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestDocConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("io-kontakt")
                .pathsToMatch("/public/v1/**")
                .packagesToScan("io.kontakt.apps.temerature.analytics.api.controller")
                .build();
    }

}
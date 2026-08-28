package com.tech.agendaai.company.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.abacatepay.url")
@Getter
@Setter
public class AbacatePayProperties {
    private String client;
    private String product;
    private String baseUrl;
    private String apiKey;
}

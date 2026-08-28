package com.tech.agendaai.company.service.abacatepay;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AbacatePayHttpClient {

    private final RestClient restClient;

    public AbacatePayHttpClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public <T, R> R send(T t, Class<R> response, String path) {
        return restClient
                .post()
                .uri(path)
                .body(t)
                .retrieve()
                .toEntity(response)
                .getBody();

    }
}

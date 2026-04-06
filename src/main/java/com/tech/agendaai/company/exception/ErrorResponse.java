package com.tech.agendaai.company.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ErrorResponse {
    private Instant timestamp;
    private final String status;
    private final int code;
    private final String message;
    private final String path;

    public ErrorResponse(String status, int code, String message, String path) {
        timestamp = Instant.now();
        this.status = status;
        this.code = code;
        this.message = message;
        this.path = path;
    }
}

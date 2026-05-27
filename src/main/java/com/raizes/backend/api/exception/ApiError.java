package com.raizes.backend.api.exception;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {

    private String error;
    private String message;
    private List<String> details;
    private LocalDateTime timestamp;
    private String path;

    public ApiError(String error, String message,
                    String path) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ApiError(String error, String message,
                    List<String> details, String path) {
        this.error = error;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
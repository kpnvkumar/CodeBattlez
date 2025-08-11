// File: src/main/java/com/codingbattle/dto/SuccessResponse.java
package com.example.codingbattle.dto;

public class SuccessResponse {
    private final String message;
    private final long timestamp = System.currentTimeMillis();

    public SuccessResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}
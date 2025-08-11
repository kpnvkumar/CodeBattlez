// File: src/main/java/com/codingbattle/exception/CodingBattleException.java
package com.example.codingbattle.exception;

import org.springframework.http.HttpStatus;

public class CodingBattleException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public CodingBattleException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CodingBattleException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public CodingBattleException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public CodingBattleException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // Static factory methods for common exceptions
    public static CodingBattleException roomNotFound(String roomId) {
        return new CodingBattleException(
                "Room not found: " + roomId,
                "ROOM_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }

    public static CodingBattleException roomNotActive(String roomId) {
        return new CodingBattleException(
                "Room is not active: " + roomId,
                "ROOM_NOT_ACTIVE",
                HttpStatus.FORBIDDEN
        );
    }

    public static CodingBattleException codeExecutionFailed(String reason) {
        return new CodingBattleException(
                "Code execution failed: " + reason,
                "CODE_EXECUTION_FAILED",
                HttpStatus.BAD_REQUEST
        );
    }

    public static CodingBattleException unsupportedLanguage(String language) {
        return new CodingBattleException(
                "Unsupported programming language: " + language,
                "UNSUPPORTED_LANGUAGE",
                HttpStatus.BAD_REQUEST
        );
    }

    public static CodingBattleException submissionFailed(String reason) {
        return new CodingBattleException(
                "Submission failed: " + reason,
                "SUBMISSION_FAILED",
                HttpStatus.BAD_REQUEST
        );
    }
}
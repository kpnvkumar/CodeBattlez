package com.example.codingbattle.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SubmissionRequest {

    @NotBlank(message = "Room ID is required")
    private String roomId;

    @NotBlank(message = "User name is required")
    private String userName;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Language is required")
    @Pattern(regexp = "^(python|java|cpp|c|javascript)$",
            message = "Language must be one of: python, java, cpp, c, javascript")
    private String language;

    // Constructors
    public SubmissionRequest() {}

    public SubmissionRequest(String roomId, String userName, String code, String language) {
        this.roomId = roomId;
        this.userName = userName;
        this.code = code;
        this.language = language;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
package com.example.codingbattle.dto;

import jakarta.validation.constraints.NotBlank;

public class JoinRoomRequest {

    @NotBlank(message = "Room ID is required")
    private String roomId;

    private String userName;

    // Constructors
    public JoinRoomRequest() {}

    public JoinRoomRequest(String roomId) {
        this.roomId = roomId;
    }

    public JoinRoomRequest(String roomId, String userName) {
        this.roomId = roomId;
        this.userName = userName;
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
}
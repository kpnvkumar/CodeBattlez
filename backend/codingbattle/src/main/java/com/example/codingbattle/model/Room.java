// File: src/main/java/com/example/codingbattle/model/Room.java
package com.example.codingbattle.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed; // Import this
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
public class Room {

    @Id
    private String id; // Internal MongoDB ID

    @Indexed(unique = true) // ADDED: Ensures every room has a unique shortId
    private String shortId; // ADDED: This will be our user-facing Room ID

    private String name;
    private String question;
    private List<TestCase> testCases;
    private String createdBy;
    private boolean isActive;
    private int maxParticipants;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Room() {
        this.testCases = new ArrayList<>();
        this.isActive = true;
        this.maxParticipants = 50;
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // ADDED: Getter and Setter for the new field
    public String getShortId() { return shortId; }
    public void setShortId(String shortId) { this.shortId = shortId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<TestCase> getTestCases() { return testCases; }
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = (testCases != null) ? testCases : new ArrayList<>();
    }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
// File: src/main/java/com/codingbattle/model/Submission.java
package com.example.codingbattle.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "submissions")
public class Submission {

    @Id
    private String id;
    @Indexed
    private String roomId;
    private String userName;
    private String code;
    private String language;
    private boolean allTestCasesPassed;
    private int testCasesPassed;
    private int totalTestCases;
    private Long executionTimeMs;
    private Long memoryUsedKb;
    private LocalDateTime submittedAt = LocalDateTime.now();
    public Submission() {}
    public Submission(String roomId, String userName, String code, String language, boolean allTestCasesPassed, int testCasesPassed, int totalTestCases) {
        this.roomId = roomId;
        this.userName = userName;
        this.code = code;
        this.language = language;
        this.allTestCasesPassed = allTestCasesPassed;
        this.testCasesPassed = testCasesPassed;
        this.totalTestCases = totalTestCases;
    }

    // Getters and Setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public boolean isAllTestCasesPassed() { return allTestCasesPassed; }
    public void setAllTestCasesPassed(boolean allTestCasesPassed) { this.allTestCasesPassed = allTestCasesPassed; }
    public int getTestCasesPassed() { return testCasesPassed; }
    public void setTestCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; }
    public int getTotalTestCases() { return totalTestCases; }
    public void setTotalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public Long getMemoryUsedKb() { return memoryUsedKb; }
    public void setMemoryUsedKb(Long memoryUsedKb) { this.memoryUsedKb = memoryUsedKb; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
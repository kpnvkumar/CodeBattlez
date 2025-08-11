// File: src/main/java/com/example/codingbattle/dto/CreateRoomRequest.java
package com.example.codingbattle.dto;

import com.example.codingbattle.model.TestCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateRoomRequest {

    @NotBlank(message = "Room name is required")
    @Size(min = 3, max = 50, message = "Room name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Question is required")
    @Size(min = 10, max = 5000)
    private String question;

    @NotEmpty(message = "At least one test case is required")
    @Size(max = 20)
    @Valid
    private List<TestCase> testCases;

    // This field corresponds to the 'owner' sent from the frontend
    private String owner;

    // Getters and Setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
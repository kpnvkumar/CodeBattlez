// File: src/main/java/com/example/codingbattle/repository/SubmissionRepository.java
package com.example.codingbattle.repository;

import com.example.codingbattle.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query; // Import this

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission, String> {

    // Method to find all submissions for a given room
    List<Submission> findByRoomId(String roomId);

    // FIX: Add this method to resolve the error
    long countByRoomId(String roomId);

    // You will also need this method for the getRoomStats functionF
    long countByRoomIdAndAllTestCasesPassedTrue(String roomId, boolean allTestCasesPassed);

    // And this one for the top performers list
    // This is a more complex query that often requires the @Query annotation
    @Query(value = "{ 'roomId': ?0, 'allTestCasesPassed': true }", sort = "{ 'submittedAt': 1 }")
    List<Submission> findTopPerformersByRoom(String roomId);
}
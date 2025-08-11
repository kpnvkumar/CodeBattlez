// src/main/java/com/codingbattle/service/SubmissionService.java
package com.example.codingbattle.service;

import com.example.codingbattle.model.Submission;
import com.example.codingbattle.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission createSubmission(Submission submission) {
        // In a real app, you would run the code against test cases here
        // and set `allTestCasesPassed` based on the results.
        // For now, we trust the client's placeholder logic.
        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByRoomId(String roomId) {
        return submissionRepository.findByRoomId(roomId);
    }
}
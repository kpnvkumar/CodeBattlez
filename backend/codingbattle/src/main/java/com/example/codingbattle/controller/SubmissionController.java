package com.example.codingbattle.controller;

import com.example.codingbattle.dto.CodeExecutionRequest;
import com.example.codingbattle.dto.CodeExecutionResult;
import com.example.codingbattle.dto.SubmissionRequest;
import com.example.codingbattle.model.Room;
import com.example.codingbattle.model.Submission;
import com.example.codingbattle.model.TestCase;
import com.example.codingbattle.service.CodeExecutionService;
import com.example.codingbattle.service.RoomService;
import com.example.codingbattle.service.SubmissionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionController.class);

    private final SubmissionService submissionService;
    private final CodeExecutionService codeExecutionService;
    private final RoomService roomService;

    @Autowired
    public SubmissionController(SubmissionService submissionService,
                                CodeExecutionService codeExecutionService,
                                RoomService roomService) {
        this.submissionService = submissionService;
        this.codeExecutionService = codeExecutionService;
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<SubmissionResult> submitCode(@Valid @RequestBody SubmissionRequest request) {
        logger.info("Processing code submission for room: {} by user: {}", request.getRoomId(), request.getUserName());

        // Get the room and its test cases
        Optional<Room> roomOpt = roomService.getRoomByShortId(request.getRoomId());
        if (roomOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new SubmissionResult(false, "Room not found", null, 0, 0)
            );
        }

        Room room = roomOpt.get();
        List<TestCase> testCases = room.getTestCases();

        if (testCases.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new SubmissionResult(false, "No test cases found for this room", null, 0, 0)
            );
        }

        // Execute code against all test cases
        int passedTests = 0;
        long totalExecutionTime = 0;
        StringBuilder detailedResults = new StringBuilder();

        for (int i = 0; i < testCases.size(); i++) {
            TestCase testCase = testCases.get(i);

            // Create the service's inner class request object
            CodeExecutionService.CodeExecutionRequest execRequest = createServiceRequest(
                    request.getCode(),
                    request.getLanguage(),
                    testCase.getInput(),
                    10, // 10 seconds timeout
                    256 // 256 MB memory limit
            );

            CodeExecutionService.CodeExecutionResult serviceResult = codeExecutionService.executeCode(execRequest);

            // Convert service result to DTO (if needed for consistent processing)
            CodeExecutionResult result = convertToDto(serviceResult);

            if (result.getExecutionTimeMs() != null) {
                totalExecutionTime += result.getExecutionTimeMs();
            }

            boolean testPassed = result.isSuccess() &&
                    result.getOutput() != null &&
                    result.getOutput().trim().equals(testCase.getExpectedOutput().trim());

            if (testPassed) {
                passedTests++;
            }

            // Build detailed results
            detailedResults.append(String.format("Test Case %d: %s\n",
                    i + 1, testPassed ? "PASSED" : "FAILED"));

            if (!testPassed) {
                detailedResults.append(String.format("  Expected: %s\n", testCase.getExpectedOutput()));
                detailedResults.append(String.format("  Got: %s\n", result.getOutput()));
                if (result.getError() != null && !result.getError().isEmpty()) {
                    detailedResults.append(String.format("  Error: %s\n", result.getError()));
                }
            }
        }

        boolean allTestsPassed = passedTests == testCases.size();

        // Create and save submission
        Submission submission = new Submission();
        submission.setRoomId(request.getRoomId());
        submission.setUserName(request.getUserName());
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setAllTestCasesPassed(allTestsPassed);
        submission.setTestCasesPassed(passedTests);
        submission.setTotalTestCases(testCases.size());
        submission.setExecutionTimeMs(totalExecutionTime);
        submission.setSubmittedAt(LocalDateTime.now());

        Submission savedSubmission = submissionService.createSubmission(submission);

        // Return result
        SubmissionResult result = new SubmissionResult(
                allTestsPassed,
                allTestsPassed ? "All test cases passed!" : "Some test cases failed",
                detailedResults.toString(),
                passedTests,
                testCases.size()
        );

        logger.info("Submission completed for user: {} - {}/{} tests passed",
                request.getUserName(), passedTests, testCases.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Submission>> getSubmissionsByRoom(@PathVariable String roomId) {
        logger.debug("Fetching submissions for room: {}", roomId);
        List<Submission> submissions = submissionService.getSubmissionsByRoomId(roomId);
        return ResponseEntity.ok(submissions);
    }

    // Helper methods to convert between DTO and service inner classes
    private CodeExecutionService.CodeExecutionRequest createServiceRequest(String code, String language,
                                                                           String input, Integer timeLimit,
                                                                           Integer memoryLimit) {
        // Since we don't know the exact structure of the inner class, we'll need to adapt this
        // This is a placeholder - you'll need to adjust based on your actual CodeExecutionService inner class
        try {
            CodeExecutionService.CodeExecutionRequest serviceRequest = new CodeExecutionService.CodeExecutionRequest();

            // Use reflection or direct method calls based on the actual inner class structure
            // You may need to adjust these method calls based on your actual implementation
            serviceRequest.setCode(code);
            serviceRequest.setLanguage(language);
            serviceRequest.setInput(input);
            serviceRequest.setTimeLimit(timeLimit);
            serviceRequest.setMemoryLimit(memoryLimit);

            return serviceRequest;
        } catch (Exception e) {
            logger.error("Error creating service request", e);
            throw new RuntimeException("Failed to create code execution request", e);
        }
    }

    private CodeExecutionResult convertToDto(CodeExecutionService.CodeExecutionResult serviceResult) {
        // Convert the service result to DTO
        // You'll need to adjust this based on your actual CodeExecutionResult DTO structure
        CodeExecutionResult dto = new CodeExecutionResult();
        dto.setSuccess(serviceResult.isSuccess());
        dto.setOutput(serviceResult.getOutput());
        dto.setError(serviceResult.getError());
        dto.setExecutionTimeMs(serviceResult.getExecutionTimeMs());

        return dto;
    }

    // Inner class for submission response
    public static class SubmissionResult {
        private final boolean success;
        private final String message;
        private final String details;
        private final int testCasesPassed;
        private final int totalTestCases;

        public SubmissionResult(boolean success, String message, String details, int testCasesPassed, int totalTestCases) {
            this.success = success;
            this.message = message;
            this.details = details;
            this.testCasesPassed = testCasesPassed;
            this.totalTestCases = totalTestCases;
        }

        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
        public int getTestCasesPassed() { return testCasesPassed; }
        public int getTotalTestCases() { return totalTestCases; }
    }
}
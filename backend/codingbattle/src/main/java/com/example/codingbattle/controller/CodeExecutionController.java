// File: src/main/java/com/codingbattle/controller/CodeExecutionController.java
package com.example.codingbattle.controller;
import com.example.codingbattle.service.CodeExecutionService.CodeExecutionRequest;
import com.example.codingbattle.service.CodeExecutionService.CodeExecutionResult;
import com.example.codingbattle.dto.ErrorResponse;
import com.example.codingbattle.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/execute")
public class CodeExecutionController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping
    public ResponseEntity<?> executeCode(@RequestBody CodeExecutionRequest request) {
        try {
            CodeExecutionResult result = codeExecutionService.executeCode(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Code execution failed: " + e.getMessage()));
        }
    }
}

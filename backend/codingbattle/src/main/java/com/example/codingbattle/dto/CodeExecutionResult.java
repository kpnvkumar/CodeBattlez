package com.example.codingbattle.dto;

public class CodeExecutionResult {
    private boolean success;
    private String output;
    private String error;
    private Long executionTimeMs; // ✅ Needed for SubmissionController

    public CodeExecutionResult() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
}

// File: src/main/java/com/example/codingbattle/service/CodeExecutionService.java
package com.example.codingbattle.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CodeExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(CodeExecutionService.class);
    private static final int TIMEOUT_SECONDS = 10;
    private static final Pattern JAVA_CLASS_PATTERN = Pattern.compile("public\\s+class\\s+([A-Za-z_$][A-Za-z0-9_$]*)");

    public CodeExecutionResult executeCode(CodeExecutionRequest request) {
        Path tempDir = null;
        try {
            // Create a unique temporary directory for this execution.
            tempDir = Files.createTempDirectory("exec-" + UUID.randomUUID());
            String lang = request.getLanguage().trim().toLowerCase();

            switch (lang) {
                case "python":
                    return executePython(request.getCode(), request.getInput(), tempDir);
                case "java":
                    return executeJava(request.getCode(), request.getInput(), tempDir);
                case "cpp", "c++":
                    return executeCpp(request.getCode(), request.getInput(), tempDir);
                case "c":
                    return executeC(request.getCode(), request.getInput(), tempDir);
                case "javascript", "js":
                    return executeJavaScript(request.getCode(), request.getInput(), tempDir);
                default:
                    return new CodeExecutionResult(false, "", "Unsupported language: " + request.getLanguage(), 0L);
            }
        } catch (Exception e) {
            logger.error("A critical error occurred during code execution.", e);
            return new CodeExecutionResult(false, "", "An unexpected execution error occurred.", 0L);
        } finally {
            // Always clean up the directory and its contents
            if (tempDir != null) {
                deleteDirectoryRecursively(tempDir);
            }
        }
    }

    private CodeExecutionResult executePython(String code, String input, Path tempDir) throws IOException, InterruptedException {
        Path sourceFile = tempDir.resolve("script.py");
        Files.writeString(sourceFile, code);
        ProcessBuilder pb = new ProcessBuilder("python3", sourceFile.toString());
        return runProcess(pb, input);
    }

    private CodeExecutionResult executeJava(String code, String input, Path tempDir) throws IOException, InterruptedException {
        Matcher matcher = JAVA_CLASS_PATTERN.matcher(code);
        if (!matcher.find()) {
            return new CodeExecutionResult(false, "", "Could not find a valid public Java class.", 0L);
        }
        String className = matcher.group(1);
        Path sourceFile = tempDir.resolve(className + ".java");
        Files.writeString(sourceFile, code);

        // Compile
        ProcessBuilder compilePb = new ProcessBuilder("javac", sourceFile.toString());
        CodeExecutionResult compileResult = runProcess(compilePb, "");
        if (!compileResult.isSuccess()) {
            return new CodeExecutionResult(false, "", "Compilation Error:\n" + compileResult.getError(), compileResult.getExecutionTimeMs());
        }

        // Run
        ProcessBuilder runPb = new ProcessBuilder("java", "-cp", tempDir.toString(), className);
        return runProcess(runPb, input);
    }

    private CodeExecutionResult executeCpp(String code, String input, Path tempDir) throws IOException, InterruptedException {
        Path sourceFile = tempDir.resolve("main.cpp");
        Path outputFile = tempDir.resolve("main.out");
        Files.writeString(sourceFile, code);

        // Compile
        ProcessBuilder compilePb = new ProcessBuilder("g++", sourceFile.toString(), "-o", outputFile.toString());
        CodeExecutionResult compileResult = runProcess(compilePb, "");
        if (!compileResult.isSuccess()) {
            return new CodeExecutionResult(false, "", "Compilation Error:\n" + compileResult.getError(), compileResult.getExecutionTimeMs());
        }

        // Run
        ProcessBuilder runPb = new ProcessBuilder(outputFile.toString());
        return runProcess(runPb, input);
    }

    private CodeExecutionResult executeC(String code, String input, Path tempDir) throws IOException, InterruptedException {
        Path sourceFile = tempDir.resolve("main.c");
        Path outputFile = tempDir.resolve("main.out");
        Files.writeString(sourceFile, code);

        // Compile
        ProcessBuilder compilePb = new ProcessBuilder("gcc", sourceFile.toString(), "-o", outputFile.toString());
        CodeExecutionResult compileResult = runProcess(compilePb, "");
        if (!compileResult.isSuccess()) {
            return new CodeExecutionResult(false, "", "Compilation Error:\n" + compileResult.getError(), compileResult.getExecutionTimeMs());
        }

        // Run
        ProcessBuilder runPb = new ProcessBuilder(outputFile.toString());
        return runProcess(runPb, input);
    }

    private CodeExecutionResult executeJavaScript(String code, String input, Path tempDir) throws IOException, InterruptedException {
        Path sourceFile = tempDir.resolve("script.js");
        Files.writeString(sourceFile, code);
        ProcessBuilder pb = new ProcessBuilder("node", sourceFile.toString());
        return runProcess(pb, input);
    }

    private CodeExecutionResult runProcess(ProcessBuilder pb, String input) throws IOException, InterruptedException {
        long startTime = System.nanoTime();
        Process process = pb.start();

        if (input != null && !input.isEmpty()) {
            try (OutputStream os = process.getOutputStream()) {
                os.write(input.getBytes());
            }
        }

        if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            return new CodeExecutionResult(false, "", "Execution timed out.", (long)TIMEOUT_SECONDS * 1000);
        }

        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        String output = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
        String error = new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().collect(Collectors.joining("\n"));

        boolean success = process.exitValue() == 0;
        return new CodeExecutionResult(success, output.trim(), error.trim(), durationMs);
    }

    private void deleteDirectoryRecursively(Path dir) {
        try {
            if (dir != null && Files.exists(dir)) {
                Files.walk(dir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            logger.error("Failed to delete temp directory: {}", dir, e);
        }
    }

    public static class CodeExecutionResult {
        private final boolean success;
        private final String output;
        private final String error;
        private final Long executionTimeMs;

        public CodeExecutionResult(boolean success, String output, String error, Long executionTimeMs) {
            this.success = success; this.output = output; this.error = error; this.executionTimeMs = executionTimeMs;
        }
        public boolean isSuccess() { return success; }
        public String getOutput() { return output; }
        public String getError() { return error; }
        public Long getExecutionTimeMs() { return executionTimeMs; }
    }

    public static class CodeExecutionRequest {
        private String code;
        private String language;
        private String input;

        public String getCode() { return code; }
        public String getLanguage() { return language; }
        public String getInput() { return input; }
    }
}

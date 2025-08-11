package com.example.codingbattle.service;

import com.example.codingbattle.dto.CodeExecutionRequest;
import com.example.codingbattle.dto.CodeExecutionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DockerCodeExecutionService {

    private static final int TIMEOUT_SECONDS = 10;
    private static final Pattern JAVA_CLASS_PATTERN = Pattern.compile("public\\s+class\\s+([A-Za-z_$][A-Za-z0-9_$]*)");

    @Value("${python.command:python}")
    private String pythonCommand;

    // Injects the sandbox command from application.properties
    @Value("${sandbox.command.prefix:}")
    private String sandboxCommandPrefix;

    public CodeExecutionResult executeCode(CodeExecutionRequest request) {
        // Create a unique temporary directory on the host for this execution.
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("exec-" + UUID.randomUUID().toString());
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
                    return new CodeExecutionResult(false, "", "Unsupported language: " + request.getLanguage());
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return new CodeExecutionResult(false, "", "An unexpected execution error occurred.");
        } finally {
            // Always clean up the host directory and its contents
            if (tempDir != null) {
                deleteDirectoryRecursively(tempDir);
            }
        }
    }

    private CodeExecutionResult executePython(String code, String input, Path tempDir) throws Exception {
        Path sourceFile = tempDir.resolve("script.py");
        Files.write(sourceFile, code.getBytes());
        // The command to run inside the sandbox
        List<String> command = List.of(pythonCommand, "script.py");
        ProcessBuilder pb = buildProcess(command, tempDir);
        return runProcess(pb, input);
    }

    private CodeExecutionResult executeJava(String code, String input, Path tempDir) throws Exception {
        Matcher matcher = JAVA_CLASS_PATTERN.matcher(code);
        if (!matcher.find()) {
            return new CodeExecutionResult(false, "", "Could not find a valid public Java class.");
        }
        String className = matcher.group(1);
        Path sourceFile = tempDir.resolve(className + ".java");
        Files.write(sourceFile, code.getBytes());

        // The combined command to compile and then run, for the sandbox
        String compileAndRunCommand = String.format("javac %s && java %s", sourceFile.getFileName(), className);
        List<String> command = List.of("/bin/sh", "-c", compileAndRunCommand);
        ProcessBuilder pb = buildProcess(command, tempDir);
        return runProcess(pb, input);
    }

    private CodeExecutionResult executeCpp(String code, String input, Path tempDir) throws Exception {
        Path sourceFile = tempDir.resolve("main.cpp");
        Files.write(sourceFile, code.getBytes());
        String compileAndRunCommand = "g++ main.cpp -o main.out && ./main.out";
        List<String> command = List.of("/bin/sh", "-c", compileAndRunCommand);
        ProcessBuilder pb = buildProcess(command, tempDir);
        return runProcess(pb, input);
    }

    private CodeExecutionResult executeC(String code, String input, Path tempDir) throws Exception {
        Path sourceFile = tempDir.resolve("main.c");
        Files.write(sourceFile, code.getBytes());
        String compileAndRunCommand = "gcc main.c -o main.out && ./main.out";
        List<String> command = List.of("/bin/sh", "-c", compileAndRunCommand);
        ProcessBuilder pb = buildProcess(command, tempDir);
        return runProcess(pb, input);
    }

    private CodeExecutionResult executeJavaScript(String code, String input, Path tempDir) throws Exception {
        Path sourceFile = tempDir.resolve("script.js");
        Files.write(sourceFile, code.getBytes());
        List<String> command = List.of("node", "script.js");
        ProcessBuilder pb = buildProcess(command, tempDir);
        return runProcess(pb, input);
    }

    private ProcessBuilder buildProcess(List<String> command, Path tempDir) {
        List<String> fullCommand;
        if (sandboxCommandPrefix != null && !sandboxCommandPrefix.trim().isEmpty()) {
            // Dynamically replace the placeholder with the actual temp directory path
            String processedPrefix = sandboxCommandPrefix.replace("#{tempDir}", tempDir.toAbsolutePath().toString());
            List<String> prefixParts = Arrays.asList(processedPrefix.trim().split("\\s+"));

            // Combine the prefix and the command
            fullCommand = Stream.concat(prefixParts.stream(), command.stream()).collect(Collectors.toList());
        } else {
            // Fallback for local testing (INSECURE)
            System.err.println("WARNING: Sandbox prefix not set. Running command directly.");
            fullCommand = new ArrayList<>(command);
        }

        ProcessBuilder pb = new ProcessBuilder(fullCommand);
        // If not using a sandbox, we must set the working directory for the process.
        // For a sandbox, this is less critical as the sandbox handles the context.
        if (sandboxCommandPrefix == null || sandboxCommandPrefix.trim().isEmpty()){
            pb.directory(tempDir.toFile());
        }
        return pb;
    }

    private CodeExecutionResult runProcess(ProcessBuilder pb, String input) throws IOException, InterruptedException {
        Process process = pb.start();

        if (input != null && !input.isEmpty()) {
            try (PrintWriter writer = new PrintWriter(process.getOutputStream())) {
                writer.print(input);
                writer.flush();
            }
        }

        if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            return new CodeExecutionResult(false, "", "Timeout exceeded");
        }

        String output = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.joining("\n"));
        String error = new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().collect(Collectors.joining("\n"));

        boolean success = process.exitValue() == 0;
        return new CodeExecutionResult(success, output.trim(), success ? "" : error.trim());
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
            System.err.println("Failed to delete temp directory: " + dir + " - " + e.getMessage());
        }
    }
}
package com.example.codingbattle.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

// Custom validation annotation for supported languages
@Documented
@Constraint(validatedBy = SupportedLanguage.SupportedLanguageValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedLanguage {
    String message() default "Unsupported programming language";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class SupportedLanguageValidator implements ConstraintValidator<SupportedLanguage, String> {
        private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
                "python", "java", "cpp", "c", "javascript"
        );

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value != null && SUPPORTED_LANGUAGES.contains(value.toLowerCase().trim());
        }
    }
}

// Custom validation for code content
@Documented
@Constraint(validatedBy = ValidCode.ValidCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface ValidCode {
    String message() default "Invalid code content";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class ValidCodeValidator implements ConstraintValidator<ValidCode, String> {
        private static final int MAX_CODE_LENGTH = 50000; // 50KB
        private static final List<String> DANGEROUS_PATTERNS = Arrays.asList(
                "import os", "import subprocess", "exec(", "eval(",
                "Runtime.getRuntime()", "ProcessBuilder", "system(", "__import__"
        );

        @Override
        public boolean isValid(String code, ConstraintValidatorContext context) {
            if (code == null || code.trim().isEmpty()) {
                return false;
            }

            if (code.length() > MAX_CODE_LENGTH) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Code exceeds maximum length of " + MAX_CODE_LENGTH + " characters")
                        .addConstraintViolation();
                return false;
            }

            // Check for potentially dangerous patterns
            for (String pattern : DANGEROUS_PATTERNS) {
                if (code.toLowerCase().contains(pattern.toLowerCase())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Code contains potentially unsafe operations")
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
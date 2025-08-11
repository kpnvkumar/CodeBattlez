// File: src/main/java/com/codingbattle/util/CodeLanguageUtils.java
package com.example.codingbattle.util;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class CodeLanguageUtils {
    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "python", "java", "cpp", "c", "javascript"
    );
    public static final Map<String, String> LANGUAGE_EXTENSIONS = new HashMap<String, String>() {{
        put("python", ".py");
        put("java", ".java");
        put("cpp", ".cpp");
        put("c", ".c");
        put("javascript", ".js");
    }};
    public static final Map<String, String> LANGUAGE_DISPLAY_NAMES = new HashMap<String, String>() {{
        put("python", "Python");
        put("java", "Java");
        put("cpp", "C++");
        put("c", "C");
        put("javascript", "JavaScript");
    }};
    public static final Map<String, String> DEFAULT_CODE_TEMPLATES = new HashMap<String, String>() {{
        put("python", "# Write your Python solution here\ndef solution():\n    # Your code here\n    pass\n\n# Example usage\n# result = solution()\n# print(result)");
        put("java", "public class Solution {\n    public static void main(String[] args) {\n        // Write your Java solution here\n        Solution sol = new Solution();\n        // Your code here\n    }\n    \n    // Add your methods here\n}");
        put("cpp", "#include <iostream>\n#include <vector>\n#include <string>\nusing namespace std;\n\nint main() {\n    // Write your C++ solution here\n    \n    return 0;\n}");
        put("c", "#include <stdio.h>\n#include <stdlib.h>\n#include <string.h>\n\nint main() {\n    // Write your C solution here\n    \n    return 0;\n}");
        put("javascript", "// Write your JavaScript solution here\nfunction solution() {\n    // Your code here\n}\n\n// Example usage\n// const result = solution();\n// console.log(result);");
    }};
    public static boolean isLanguageSupported(String language) {
        return language != null && SUPPORTED_LANGUAGES.contains(language.toLowerCase());
    }
    public static String getFileExtension(String language) {
        return LANGUAGE_EXTENSIONS.getOrDefault(language.toLowerCase(), ".txt");
    }
    public static String getDisplayName(String language) {
        return LANGUAGE_DISPLAY_NAMES.getOrDefault(language.toLowerCase(), language);
    }
    public static String getDefaultTemplate(String language) {
        return DEFAULT_CODE_TEMPLATES.getOrDefault(language.toLowerCase(), "// Write your code here");
    }
}
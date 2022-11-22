package utils;

import java.util.List;

public class ErrorUtils {
    public static String errorsToString(List<String> errors) {
        return String.join(";\n", errors);
    }
}

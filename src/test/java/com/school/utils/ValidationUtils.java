package com.school.utils;

import java.util.UUID;

public class ValidationUtils {
    public static boolean codeIsValidUUID(String code) {
        try {
            UUID.fromString(code);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }
}

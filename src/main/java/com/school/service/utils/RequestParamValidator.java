package com.school.service.utils;

import com.school.model.OptionalRequestParams;

public class RequestParamValidator {
    private static Long validate(Long id) {
        if (id == null || id == 0) {
            return null;
        } else {
            return id;
        }
    }

    private static String validate(String param) {
        if (param == null || param.isBlank() || param.isEmpty()) {
            return null;
        } else {
            return param;
        }
    }

    public static OptionalRequestParams prepareOptionalRequestParams(String fileType, Long id, String name, String surname, String identifier, String subject) {
        return new OptionalRequestParams(
                validate(fileType),
                validate(id),
                validate(name),
                validate(surname),
                validate(identifier),
                validate(subject)
        );
    }
}

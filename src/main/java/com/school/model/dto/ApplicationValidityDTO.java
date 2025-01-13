package com.school.model.dto;

import com.school.model.enums.Validity;

public class ApplicationValidityDTO {
    private Validity validity;
    private String latestVersion;
    private String message;

    public ApplicationValidityDTO(final Validity validity, final String latestVersion, final String message) {
        this.validity = validity;
        this.latestVersion = latestVersion;
        this.message = message;
    }

    public ApplicationValidityDTO() {
    }

    public Validity getValidity() {
        return validity;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getMessage() {
        return message;
    }
}

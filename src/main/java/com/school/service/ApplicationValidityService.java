package com.school.service;

import com.school.model.enums.Validity;
import org.springframework.stereotype.Service;

@Service
public class ApplicationValidityService {
    private Validity validity = Validity.NOT_DETERMINED_YET;

    public void setApplicationValidity(Validity validity) {
        this.validity = validity;
    }

    public Validity getApplicationValidity() {
        return this.validity;
    }
}

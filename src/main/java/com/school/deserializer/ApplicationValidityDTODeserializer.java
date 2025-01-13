package com.school.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.model.dto.ApplicationValidityDTO;
import org.apache.kafka.common.serialization.Deserializer;

public class ApplicationValidityDTODeserializer implements Deserializer<ApplicationValidityDTO> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ApplicationValidityDTO deserialize(final String s, final byte[] bytes) {
        try {
            return mapper.readValue(bytes, ApplicationValidityDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize ApplicationValidityDTO", e);
        }
    }
}

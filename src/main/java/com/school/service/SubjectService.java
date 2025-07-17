package com.school.service;

import com.school.model.dto.SubjectDTO;
import com.school.model.entity.Subject;
import com.school.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectDTO> getSchoolClassSubjects(Long schoolClassId) {
        List<Subject> schoolClassSubjects = subjectRepository.findBySchoolClassId(schoolClassId);
        if (!schoolClassSubjects.isEmpty()) {
            return schoolClassSubjects.stream().map(s -> new SubjectDTO(s.getId(), s.getName())).toList();
        } else {
            return Collections.emptyList();
        }
    }
}

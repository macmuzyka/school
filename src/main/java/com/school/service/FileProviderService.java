package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileBuilder;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.schoolmodel.model.entity.Subject;
import com.schoolmodel.model.enums.FileType;
import com.schoolmodel.model.response.FileProviderResponse;
import com.schoolmodel.model.entity.Student;
import com.school.repository.GradeRepository;
import com.school.service.utils.mapper.QueryResultsMappingUtils;
import com.schoolmodel.model.dto.SubjectGradesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FileProviderService {
    private final Logger log = LoggerFactory.getLogger(FileProviderService.class);
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FileConfig fileConfig;

    public FileProviderService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, FileConfig fileConfig) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.fileConfig = fileConfig;
    }

    public FileProviderResponse getProperFile(String fileType, String studentId, String subjectName) throws IllegalAccessException {
        FileType resolvedFileType;
        try {
            resolvedFileType = FileType.valueOf(fileType.toUpperCase());
        } catch (Exception e) {
            List<String> values = Arrays.stream(FileType.values()).map(Enum::toString).toList();
            throw new IllegalAccessException("Declared file type [" + fileType + "] not supported yet! Available types are: " + values);
        }
        String parametrizedFilePrefix = preparePrefixFromParameters(studentId, subjectName);
        FileBuilder fileBuilder = PreparationStrategy.resolve(resolvedFileType, fileConfig, parametrizedFilePrefix);
        return fileBuilder.prepare(getDataTransferObjects(Long.parseLong(studentId), subjectName));
    }

    private String preparePrefixFromParameters(String studentId, String subjectName) {
        long properId = longValueFromRequestParameter(studentId);
        Student foundStudent = studentRepository.findById(properId).orElse(null);
        Subject foundSubject = subjectRepository.findFirstByName(subjectName).orElse(null);

        if (foundStudent != null && foundSubject != null) {
            return foundStudent.getName() + "_" + foundStudent.getSurname() + "_" + foundSubject.getName() + "_";
        } else if (foundStudent != null) {
            return foundStudent.getName() + "_" + foundStudent.getSurname() + "_";
        } else if (foundSubject != null) {
            return foundSubject.getName() + "_";
        } else {
            return "";
        }
    }

    private long longValueFromRequestParameter(String requestParamStudentId) {
        return Long.parseLong(requestParamStudentId);
    }

    public List<SubjectGradesDTO> getDataTransferObjects(long studentId, String subjectName) {
        Long longValueForQuery = prepareLongValueForRepositoryQuery(studentId);

        return gradeRepository.findAllGradesGroupedBySubject(longValueForQuery, subjectName).stream()
                .map(QueryResultsMappingUtils::buildSubjectGradesObject)
                .toList();
    }

    private Long prepareLongValueForRepositoryQuery(long studentId) {
        if (studentId == 0) {
            return null;
        } else {
            return studentId;
        }
    }
}

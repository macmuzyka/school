package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.FileProvider;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.school.service.utils.FileNamePrefixResolver;
import com.school.service.utils.QueryParamValidator;
import com.schoolmodel.model.entity.Subject;
import com.schoolmodel.model.enums.FileType;
import com.schoolmodel.model.response.FileProviderResponse;
import com.schoolmodel.model.entity.Student;
import com.school.repository.GradeRepository;
import com.school.service.utils.mapper.QueryResultsMappingUtils;
import com.schoolmodel.model.dto.StudentSubjectGradesDTO;
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

    public FileProviderResponse produceFile(String fileType, String optionalStudentId, String optionalSubjectName) {
        try {
            prepareFileTypeAndNamePrefix(fileType, optionalStudentId, optionalSubjectName);
            List<StudentSubjectGradesDTO> records = getRecordsFromDatabase(optionalStudentId, optionalSubjectName);
            return produceFileAndGetResponse(records);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void prepareFileTypeAndNamePrefix(String fileType, String optionalStudentId, String optionalSubjectName) {
        resolveFileType(fileType);
        prepareFileNamePrefix(optionalStudentId, optionalSubjectName);
    }

    private void resolveFileType(String fileType) {
        try {
            FileType resolvedFileType = FileType.valueOf(fileType.toUpperCase());
            fileConfig.setFileType(resolvedFileType);
        } catch (Exception e) {
            List<String> values = Arrays.stream(FileType.values()).map(Enum::toString).toList();
            throw new IllegalArgumentException("Declared file type [" + fileType + "] not supported yet! Available types are: " + values);
        }
    }

    private void prepareFileNamePrefix(String optionalStudentId, String optionalSubjectName) {
        long repositoryStudentId = QueryParamValidator.longValueFromRequestParameter(optionalStudentId);
        Student foundStudent = studentRepository.findById(repositoryStudentId).orElse(null);
        Subject foundSubject = subjectRepository.findFirstByName(optionalSubjectName).orElse(null);

        fileConfig.setOptionalNamePrefix(FileNamePrefixResolver.build(foundStudent, foundSubject));
    }

    private List<StudentSubjectGradesDTO> getRecordsFromDatabase(String optionalStudentId, String optionalSubjectName) {
        Long studentIdForQuery = QueryParamValidator.prepareLongValueForRepositoryQuery(optionalStudentId);
        return gradeRepository.findAllGradesGroupedBySubject(studentIdForQuery, optionalSubjectName).stream()
                .map(QueryResultsMappingUtils::buildStudentSubjectGradesObject)
                .toList();
    }
    private FileProviderResponse produceFileAndGetResponse(List<StudentSubjectGradesDTO> records) {
        FileProvider fileProvider = FileProviderStrategy.resolve(fileConfig);
        return fileProvider.build(records);
    }
}

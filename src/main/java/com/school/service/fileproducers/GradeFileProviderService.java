package com.school.service.fileproducers;

import com.school.configuration.FileConfig;
import com.school.model.*;
import com.school.model.response.FileProviderResponse;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.school.service.FileProviderStrategy;
import com.school.service.utils.FileNamePrefixResolver;
import com.school.model.entity.Subject;
import com.school.model.enums.FileType;
import com.school.model.entity.Student;
import com.school.repository.GradeRepository;
import com.school.service.utils.mapper.QueryResultsMapper;
import com.school.model.dto.StudentSubjectGradesDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.school.service.utils.FileUtils.*;

@Service
public class GradeFileProviderService implements RecentlyProducedFile {
    private final Logger log = LoggerFactory.getLogger(GradeFileProviderService.class);
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FileConfig fileConfig;

    public GradeFileProviderService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, FileConfig fileConfig) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.fileConfig = fileConfig;
    }

    @NotNull
    @Override
    public FileToImport importFile(OptionalRequestParams params) {
        String directoryPath = fileConfig.getDirectory() + File.separator + fileConfig.getStudentsGradesSubdirectory();
        File directory = new File(directoryPath);
        purifyDirectoryFromAllPreviousFiles(directory);
        produceFileAndSaveToCorrespondingDirectory(directoryPath, params);
        return prepareFileToImport(directory);
    }

    private FileToImport prepareFileToImport(File directory) {
        return validateAndPrepareFile(directory);
    }

    @Override
    public void produceFileAndSaveToCorrespondingDirectory(@NotNull String directoryPath, @Nullable OptionalRequestParams params) {
        if (params == null) {
            params = new OptionalRequestParams();
        }
        prepareFileTypeAndNamePrefix(params);
        List<StudentSubjectGradesDTO> records = getRecordsFromDatabase(params);
        FileProvider fileProvider = FileProviderStrategy.resolve(fileConfig);
        FileProviderResponse response = fileProvider.build(records);
        log.info("Response from creating file: {}", response);
    }

    private void prepareFileTypeAndNamePrefix(OptionalRequestParams params) {
        resolveFileType(params.getFileType());
        prepareFileNamePrefix(params);
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

    private void prepareFileNamePrefix(OptionalRequestParams params) {
        Student foundStudent;
        log.info("params: {}", params);
        if (params.getId() == null) {
            foundStudent = null;
        } else {
            log.info("passed id: {}", params.getId());
            foundStudent = studentRepository.findById(params.getId()).orElse(null);
        }

        Subject foundSubject = subjectRepository.findFirstByName(params.getSubject()).orElse(null);
        log.info("subject found: {}", foundSubject);
        fileConfig.setOptionalNamePrefix(FileNamePrefixResolver.build(foundStudent, foundSubject));
    }

    private List<StudentSubjectGradesDTO> getRecordsFromDatabase(OptionalRequestParams params) {
        return gradeRepository.findAllGradesGroupedBySubject(
                        params.getId(),
                        params.getSubject(),
                        params.getName(),
                        params.getSurname(),
                        params.getIdentifier())
                .stream()
                .map(QueryResultsMapper::buildStudentSubjectGradesObject)
                .toList();
    }
}

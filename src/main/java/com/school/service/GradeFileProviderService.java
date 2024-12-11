package com.school.service;

import com.school.configuration.FileConfig;
import com.school.model.*;
import com.school.repository.StudentRepository;
import com.school.repository.SubjectRepository;
import com.school.service.utils.FileNamePrefixResolver;
import com.school.model.entity.Subject;
import com.school.model.enums.FileType;
import com.school.model.response.FileProviderResponse;
import com.school.model.entity.Student;
import com.school.repository.GradeRepository;
import com.school.service.utils.mapper.QueryResultsMapper;
import com.school.model.dto.StudentSubjectGradesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class GradeFileProviderService extends RecentlyProducedFileChecker implements RecentlyProducedFile {
    private final Logger log = LoggerFactory.getLogger(GradeFileProviderService.class);
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final DirectoryPurifierService directoryPurifierService;
    private final FileConfig fileConfig;

    public GradeFileProviderService(GradeRepository gradeRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, DirectoryPurifierService directoryPurifierService, FileConfig fileConfig) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.directoryPurifierService = directoryPurifierService;
        this.fileConfig = fileConfig;
    }

    public FileToImport getFile(OptionalRequestParams params) {
        try {
            File directory = new File(fileConfig.getDirectory() + fileConfig.getStudentsGradesSubdirectory());
            directoryPurifierService.purifyDirectoryFromAllPreviousFiles(directory);
//            clearTemporaryDirectoryFromPreviousFiles();
            prepareFileTypeAndNamePrefix(params.getFileType(), params);
            List<StudentSubjectGradesDTO> records = getRecordsFromDatabase(params);
            return produceAndReturnFile(directory, records);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

//    private void clearTemporaryDirectoryFromPreviousFiles() {
//        File directory = new File();
////        checkIfDirectoryExists(directory);
//        File[] files = directory.listFiles();
//        if (files != null) {
//            deleteAllPreviousFiles(files);
//        } else {
//            log.info("No files to delete from temporary directory");
//        }
//    }

//    private void checkIfDirectoryExists(File directory) {
//        if (!directory.exists() || !directory.isDirectory()) {
//            throw new RuntimeException("Directory does not exist.");
//        }
//    }

//    private void deleteAllPreviousFiles(File[] files) {
//        for (File f : files) {
//            if (f.delete()) {
//                log.info("Deleted {} file", f.getName());
//            }
//        }
//    }

    private void prepareFileTypeAndNamePrefix(String fileType, OptionalRequestParams params) {
        resolveFileType(fileType);
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

    private FileToImport produceAndReturnFile(File directory, List<StudentSubjectGradesDTO> records) {
        FileProvider fileProvider = FileProviderStrategy.resolve(fileConfig);
        FileProviderResponse response = fileProvider.build(records);
        log.info("Response from creating file: {}", response);
//        return recentlyProducedFile();
        return getRecentlyProducedFile(directory);
    }

    @Override
    public FileToImport getRecentlyProducedFile(File directory) {
        checkIfFileExists(directory);
        File fileToReturn = getFileIfExists(directory);

        try {
            FileSystemResource resource = new FileSystemResource(fileToReturn);
            log.info("File to return name: {}", fileToReturn.getName());
            return new FileToImport(resource, fileToReturn.getName());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving the file: " + e.getMessage());
        }
    }


//    private FileToImport recentlyProducedFile() {
//        File directory = new File(fileConfig.getDirectory());
//
////        checkIfDirectoryExists(directory);
//        File[] files = getFileIfExists(directory);
//        File fileToReturn = files[0];
//
//        try {
//            FileSystemResource resource = new FileSystemResource(fileToReturn);
//            log.info("File to return name: {}", fileToReturn.getName());
//            return new FileToImport(resource, fileToReturn.getName());
//        } catch (Exception e) {
//            throw new RuntimeException("Error retrieving the file: " + e.getMessage());
//        }
//    }

//    private File[] getFileIfExists(File directory) {
//        File[] files = directory.listFiles();
//        if (files == null || files.length == 0) {
//            throw new RuntimeException("No files available.");
//        }
//        return files;
//    }
}

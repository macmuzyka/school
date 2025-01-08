package com.school.model.pgtool;

import com.school.model.response.BackupResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PgRestoreTool {
    BackupResponse execute(MultipartFile multipartFile);
}

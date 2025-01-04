package com.school.service;

import com.school.model.response.BackupResponse;
import org.springframework.web.multipart.MultipartFile;

interface PgTool {
    BackupResponse execute(MultipartFile optionalFile);
}

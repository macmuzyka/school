package com.school.model.pgtool;

import com.school.model.FileToImport;

public interface PgDumpTool {
    FileToImport execute(boolean storePermanently);
}

package com.school.model.response;

public class FileProviderResponse {
    private FileStatus fileStatus;
    private int totalRecords;
    private String message;

    public FileProviderResponse(FileStatus fileStatus, int totalRecords, String message) {
        this.fileStatus = fileStatus;
        this.totalRecords = totalRecords;
        this.message = message;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

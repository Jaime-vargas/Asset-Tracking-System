package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum FileExceptionEnum {
    INVALID_FILE("Error reading file."),
    DIRECTORY_CREATION_ERROR("Directory Creation Error, Could not create directory for saving attachment files: "),
    SAVE_ERROR("FileSaveError: "),
    DELETE_ERROR("FileDeleteError, Could not delete the file at the specified path."),
    IMAGE_FORMAT_ERROR("InvalidFileFormat, The uploaded file is not a valid image format."),
    DUPLICATE_FILE("DuplicateFile, A file with the same name already exists."),
    FILE_NOT_FOUND("FileNotFound, The requested file could not be found,"),
    ALREADY_EXISTS("FileAlreadyExists:, A file already exists.");

    private final String message;
    FileExceptionEnum(String message) {
        this.message = message;
    }

    public String getMessage(String message){
        return this.message + message;
    }
}

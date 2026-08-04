package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum OperationNotAllowedExceptionEnum {

    OPERATION_NOT_ALLOWED("Operation not allowed"),

    // CAMERA
    CAMERA_NOT_BELONG_TO_SUCURSAL("Camera does not belong to the specified sucursal"),

    // COMMENT
    COMMENT_NOT_BELONG_TO_REPORT("Comment does not belong to the specified report"),

    // FILES
    FILE_TOO_LARGE("File size exceeds the maximum allowed size of 10MB."),
    NOT_A_MULTIPART_REQUEST("Request dont have a multipart file."),

    // REPORT
    REPORT_ALREADY_CLOSED("Report is already closed"),
    REPORT_NOT_BELONG_TO_HARDWARE("Report does not belong to hardware"),
    // REPORT ENUM
    INVALID_REPORT_PRIORITY("Invalid report priority. Valid values are: LOW, MEDIUM, HIGH"),

    // REPORT DATES
    INVALID_DUE_DATE("Invalid due date. Due date must be after the current date and time"),
    INVALID_DUE_DATE_FORMAT("Invalid due date format. Expected format: yyyy-MM-dd'T'HH:mm:ssXXX"),

    // SUCURSAL
    SUCURSAL_NOT_BELONG_TO_CLIENT("Sucursal does not belong to the specified client"),

    //USER
    USER_DISABLED("The access for current user is disabled"),
    USER_PERMISSION_NOT_ALLOWED("The access is forbidden"),
    USER_ROOT_CHANGES_NOT_ALLOWED("The user root changes are not allowed"),
    USER_PASSWORD_DONT_MATCH("User old password does not match"),
    USER_COMMENT_DONT_MATCH("Actual user does not belong to comment");

    private final String message;

    OperationNotAllowedExceptionEnum(String message) {
        this.message = message;
    }
}

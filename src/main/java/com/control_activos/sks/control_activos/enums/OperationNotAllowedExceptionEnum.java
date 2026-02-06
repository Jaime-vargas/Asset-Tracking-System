package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum OperationNotAllowedExceptionEnum {

    OPERATION_NOT_ALLOWED("Operation not allowed"),

    // REPORT
    REPORT_ALREADY_CLOSED("Report is already closed"),
    REPORT_NOT_BELONG_TO_HARDWARE("Report does not belong to hardware"),

    // CAMERA
    CAMERA_NOT_BELONG_TO_SUCURSAL("Camera does not belong to the specified sucursal"),

    // SUCURSAL
    SUCURSAL_NOT_BELONG_TO_CLIENT("Sucursal does not belong to the specified client"),

    //USER
    USER_PASSWORD_DONT_MATCH("User old password does not match");

    private final String message;

    OperationNotAllowedExceptionEnum(String message) {
        this.message = message;
    }
}

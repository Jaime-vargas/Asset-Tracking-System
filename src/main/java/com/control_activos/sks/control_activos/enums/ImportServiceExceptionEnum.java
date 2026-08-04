package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum ImportServiceExceptionEnum {

    ERROR_GENERATING_TEMPLATE("Error generating template: "),
    ERROR_IMPORTING_FILE("Error importing file: ");

    final String message;

    ImportServiceExceptionEnum(String message) {
        this.message = message;
    }

    public String getMessage(String message){
        return this.message + message;
    }
}

package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ADMIN("Administrator"),
    USER("Standard User");

    private final String value;
    UserRoleEnum(String value) {
        this.value = value;
    }
}

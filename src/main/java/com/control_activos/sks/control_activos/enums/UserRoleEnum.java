package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum UserRoleEnum {
    ADMIN("Administrator"),
    USER("Standard User");

    private final String value;
    UserRoleEnum(String value) {
        this.value = value;
    }

    public static Optional<UserRoleEnum> fromValue(String value) {
        return Arrays.stream(values()).filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst();
    }
}

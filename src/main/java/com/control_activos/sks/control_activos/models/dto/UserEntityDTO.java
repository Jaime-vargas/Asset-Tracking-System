package com.control_activos.sks.control_activos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserEntityDTO {
    Long id;
    String username;
    String password;
    String fullName;
    String role;
}
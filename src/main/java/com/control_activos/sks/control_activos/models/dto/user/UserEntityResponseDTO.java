package com.control_activos.sks.control_activos.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserEntityResponseDTO {
    Long id;
    String username;
    String fullName;
    String role;
    boolean active;
}
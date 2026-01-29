package com.control_activos.sks.control_activos.models;

import com.control_activos.sks.control_activos.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;
}

package com.control_activos.sks.control_activos.models.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Camera extends Hardware {

    private String idCamera;
    private String macAddress;
    private String ipAddress;
}

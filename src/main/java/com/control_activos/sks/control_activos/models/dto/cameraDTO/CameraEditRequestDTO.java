package com.control_activos.sks.control_activos.models.dto.cameraDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CameraEditRequestDTO {
    String name;
    String brand;
    String serialNumber;
    String model;
    String location;
    String cameraId;
    String macAddress;
    String ipAddress;
    String idf;
    String username;
    String password;
}

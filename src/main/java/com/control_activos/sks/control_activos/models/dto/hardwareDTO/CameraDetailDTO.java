package com.control_activos.sks.control_activos.models.dto.hardwareDTO;


import com.control_activos.sks.control_activos.models.dto.PhotoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CameraDetailDTO extends HardwareDetailDTO {

    private String cameraId;
    private String macAddress;
    private String ipAddress;
    private String idf;
    private String username;
    private String password;
    private PhotoDTO viewFromCameraPhoto;
    private PhotoDTO viewToCameraPhoto;


    public CameraDetailDTO(Long id, String type, String brand, String model, String serialNumber, String location, String name, String lastMaintenanceDate,
                           String cameraId, String macAddress, String ipAddress, String idf, String username, String password, PhotoDTO viewFromCameraPhoto, PhotoDTO viewToCameraPhoto) {
        super(id, type, brand, model, serialNumber, location, name, lastMaintenanceDate);
        this.cameraId = cameraId;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.idf = idf;
        this.username = username;
        this.password = password;
        this.viewFromCameraPhoto = viewFromCameraPhoto;
        this.viewToCameraPhoto = viewToCameraPhoto;
    }
}

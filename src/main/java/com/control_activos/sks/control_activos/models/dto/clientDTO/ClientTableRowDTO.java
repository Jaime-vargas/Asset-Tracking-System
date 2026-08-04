package com.control_activos.sks.control_activos.models.dto.clientDTO;

import com.control_activos.sks.control_activos.models.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientTableRowDTO {
    Long id;
    String name;
    Long branches;
    Long totalHardware;
    FileEntity photo;
}

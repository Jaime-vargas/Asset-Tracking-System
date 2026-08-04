package com.control_activos.sks.control_activos.enums;

import lombok.Getter;

@Getter
public enum CameraImportColumns {
    ID(0, "ID (Optional)", 14),
    CAMERA_ID(1, "CAMERA ID", 15),
    NAME(2, "NAME", 30),
    BRAND(3, "BRAND", 18),
    MODEL(4, "MODEL", 18),
    SERIAL_NUMBER(5, "SERIAL NUMBER", 25),
    LOCATION(6, "LOCATION (Optional)", 35),
    MAC_ADDRESS(7, "MAC ADDRESS", 22),
    IP_ADDRESS(8, "IP ADDRESS", 18),
    IDF(9, "IDF (Optional)", 20),
    USERNAME(10, "USERNAME (Optional)", 24),
    PASSWORD(11, "PASSWORD (Optional)", 24);

    private final int index;
    private final String header;
    private final int cellWidth;

    CameraImportColumns(int index, String header, int cellWidth) {
        this.index = index;
        this.header = header;
        this.cellWidth = cellWidth;
    }
}

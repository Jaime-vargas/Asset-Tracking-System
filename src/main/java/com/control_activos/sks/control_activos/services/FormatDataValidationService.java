package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.ResourceFormatExceptionEnum;
import com.control_activos.sks.control_activos.exception.ResourceFormatException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class FormatDataValidationService {
    Pattern macAddressPattern = Pattern.compile("^([0-9A-F]{2}:){5}([0-9A-F]{2})$");
    Pattern ipAddressPattern = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public String validateMacAddressFormat(String macAddress) {
        if (macAddress == null) {
            throw new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_MAC_ADDRESS.getMessage());
        }
        macAddress = macAddress.toUpperCase().trim();
        macAddress = macAddress.replaceAll("[-_\\s]+", ":");
        if (!macAddressPattern.matcher(macAddress).matches()) {
            throw new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_MAC_ADDRESS.getMessage());
        }
        return macAddress;
    }

    public String validateIpAddressFormat(String ipAddress) {
        if (ipAddress == null) {
            throw new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_IP_ADDRESS.getMessage());
        }
        ipAddress = ipAddress.trim();
        if (!ipAddressPattern.matcher(ipAddress).matches()) {
            throw new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_IP_ADDRESS.getMessage());
        }
        return ipAddress;
    }
}

package com.control_activos.sks.control_activos.models.dto;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ImportResultResponse {
    int processed;
    int successful;
    int failed;
    List<String> errors;
}

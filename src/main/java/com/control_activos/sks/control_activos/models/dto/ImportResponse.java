package com.control_activos.sks.control_activos.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ImportResponse {
    int processed = 0;
    int successful = 0;
    int errorsCount = 0;
    List<String> errors = new ArrayList<>();

    public void processed (){
        this.processed++;
    }

    public void successful (){
        this.successful++;
    }

    public void error (String message){
        this.errorsCount++;
        this.errors.add(message);

    }
}

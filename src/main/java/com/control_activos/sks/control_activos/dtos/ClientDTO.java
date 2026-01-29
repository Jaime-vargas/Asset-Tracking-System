package com.control_activos.sks.control_activos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ClientDTO {
    private Long id;
    private String name;
    private List<String> sucursalList;
}


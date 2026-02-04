package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.SucursalDTO;
import com.control_activos.sks.control_activos.services.SucursalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
public class SucursalController {

    private SucursalService sucursalService;
    public SucursalController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @PostMapping("/{clientId}/sucursal")
    public ResponseEntity<SucursalDTO> saveSucursal(@PathVariable Long clientId, @RequestBody SucursalDTO sucursalDTO) {
        sucursalDTO = sucursalService.saveSucursal(clientId, sucursalDTO);
        return ResponseEntity.ok().body(sucursalDTO);
}

    @PutMapping("/{clientId}/sucursal/{sucursalId}")
    public ResponseEntity<SucursalDTO> updateSucursal(@PathVariable Long clientId, @PathVariable Long sucursalId, @RequestBody SucursalDTO sucursalDTO) {
        sucursalDTO = sucursalService.editSucursal(clientId, sucursalId, sucursalDTO);
        return ResponseEntity.ok().body(sucursalDTO);
    }
}

package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.dtos.ClientDTO;
import com.control_activos.sks.control_activos.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO createdClient = clientService.createClient(clientDTO);
        return ResponseEntity.ok(createdClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.updateClient(clientId, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

}

package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.BranchDTO;
import com.control_activos.sks.control_activos.models.dto.branchDTO.BranchTableDTO;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientDTO;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientTableDTO;
import com.control_activos.sks.control_activos.services.BranchService;
import com.control_activos.sks.control_activos.services.ClientService;
import com.control_activos.sks.control_activos.services.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final BranchService branchService;
    private final ClientService clientService;
    private final FilesService filesService;

    /** Client endpoints */
    @GetMapping
    public ResponseEntity<List<ClientTableDTO>> getAllClientTableDTO() {
        List<ClientTableDTO> clients = clientService.getAllClientTableDTO();
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient (@RequestBody ClientDTO clientDTO){
        ClientDTO createdClient = clientService.saveClient(clientDTO);
        return ResponseEntity.ok(createdClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.editClient(clientId, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    @PostMapping("/{clientId}/photo")
    public ResponseEntity<ClientDTO> addPhoto (@PathVariable Long clientId,
                                               @RequestPart("file") MultipartFile file,
                                               @RequestParam(defaultValue = "false") Boolean replaceExisting) {

        ClientDTO clientDTO = filesService.UploadClientPhoto(clientId, file, replaceExisting);
        return ResponseEntity.ok().body(clientDTO);
    }

    /** Branch related endpoints */
    @GetMapping("/{clientId}/branches")
    public ResponseEntity<List<BranchTableDTO>> getAllBranchTableDTOByClientId(@PathVariable Long clientId) {
        List<BranchTableDTO> branches = clientService.getAllBranchTableDTOByClientId(clientId);
        return ResponseEntity.ok().body(branches);
    }

    @PostMapping("/{clientId}/branches")
    public ResponseEntity<BranchDTO> saveBranch (@PathVariable Long clientId, @RequestBody BranchDTO
    branchDTO){
        branchDTO = clientService.saveBranch(clientId, branchDTO);
        return ResponseEntity.ok().body(branchDTO);
    }
}
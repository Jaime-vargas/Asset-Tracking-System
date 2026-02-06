package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.UserEntityDTO;
import com.control_activos.sks.control_activos.models.dto.UserEntityPasswordRequestDTO;
import com.control_activos.sks.control_activos.models.dto.UserEntityResponseDTO;
import com.control_activos.sks.control_activos.models.dto.UserEntityRoleDTO;
import com.control_activos.sks.control_activos.services.UserEntityService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserEntityController {

    private final UserEntityService userEntityService;
    public UserEntityController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntityResponseDTO>> getUserEntityDTOList(){
        List<UserEntityResponseDTO> userEntityDTOList = userEntityService.getUserEntityDTOList();
        return ResponseEntity.ok().body(userEntityDTOList);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<UserEntityRoleDTO>> getUserEntityRoleDTOList(){
        List<UserEntityRoleDTO> userEntityRoleDTOList = userEntityService.getUserRoleList();
        return ResponseEntity.ok().body(userEntityRoleDTOList);
    }

    @PostMapping
    public ResponseEntity<UserEntityResponseDTO> saveUserEntity(@RequestBody UserEntityDTO userEntityDTO){
        UserEntityResponseDTO savedUserEntityResponseDTO = userEntityService.saveUserEntity(userEntityDTO);
        return ResponseEntity.ok().body(savedUserEntityResponseDTO);
    }

    @PutMapping("/{userEntityId}")
    public ResponseEntity<UserEntityResponseDTO> updateUserEntity(@PathVariable Long userEntityId, @RequestBody UserEntityResponseDTO userEntityResponseDTO){
        UserEntityResponseDTO updatedUserEntityDTO = userEntityService.updateUserEntity(userEntityId, userEntityResponseDTO);
        return ResponseEntity.ok().body(updatedUserEntityDTO);
    }

    @PutMapping("/{userEntityId}/password")
    public ResponseEntity<?> updateUserEntityPassword(@PathVariable Long userEntityId, @RequestBody UserEntityPasswordRequestDTO userEntityPasswordRequestDTO){
        userEntityService.updateUserEntityPassword(userEntityId, userEntityPasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }
}

package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.dto.user.*;
import com.control_activos.sks.control_activos.services.UserEntityService;
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
    public ResponseEntity<UserEntityResponseDTO> updateUserEntity(@PathVariable Long userEntityId, @RequestBody UserEntityEditRequestDTO userEntityEditRequestDTO){
        UserEntityResponseDTO updatedUserEntityDTO = userEntityService.updateUserEntity(userEntityId, userEntityEditRequestDTO);
        return ResponseEntity.ok().body(updatedUserEntityDTO);
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> updateUserEntityPassword(@RequestBody UserEntityPasswordRequestDTO userEntityPasswordRequestDTO){
        userEntityService.updateUserEntityPassword(userEntityPasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userEntityId}/reset-password")
    public  ResponseEntity<?> resetUserEntityPassword(@PathVariable Long userEntityId, @RequestBody UserEntityResetPasswordDTO userEntityResetPasswordDTO){
        userEntityService.resetUserEntityPassword(userEntityId, userEntityResetPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userEntityId}/disable-user")
    public ResponseEntity<UserEntityResponseDTO> disableUserEntity (@PathVariable Long userEntityId){
        UserEntityResponseDTO disabledUserEntityResponseDTO = userEntityService.disableUserEntity(userEntityId);
        return ResponseEntity.ok().body(disabledUserEntityResponseDTO);
    }

    @PutMapping("/{userEntityId}/enable-user")
    public ResponseEntity<UserEntityResponseDTO> enableUserEntity (@PathVariable Long userEntityId){
        UserEntityResponseDTO enabledUserEntityDTO = userEntityService.enableUserEntity(userEntityId);
        return ResponseEntity.ok().body(enabledUserEntityDTO);
    }
}

package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.*;
import com.control_activos.sks.control_activos.exception.AuthenticationException;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceFormatException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.user.*;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;

    // Users
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntityResponseDTO> getUserEntityDTOList (){
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        return userEntityList.stream().map(Mapper::entityToDTO).toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntityResponseDTO saveUserEntity(UserEntityDTO userEntityDTO){
        UserEntity userEntity = new UserEntity();
        setDataToNewEntity(userEntity, userEntityDTO);
        userEntity = userEntityRepository.save(userEntity);
        return Mapper.entityToDTO(userEntity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntityResponseDTO updateUserEntity(Long userEntityId, UserEntityEditRequestDTO userEntityEditRequestDTO){
        UserEntity userEntity = findByUserEntityById(userEntityId);
        if(userEntity.getUsername().equals("root")){
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.USER_ROOT_CHANGES_NOT_ALLOWED.getMessage());
        }
        setDataToUpdatedEntity(userEntity, userEntityEditRequestDTO);
        userEntity = userEntityRepository.save(userEntity);
        return Mapper.entityToDTO(userEntity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntityResponseDTO disableUserEntity(Long userEntityId){
        UserEntity userEntity = findByUserEntityById(userEntityId);

        if(userEntity.getUsername().equals("root")){
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.USER_ROOT_CHANGES_NOT_ALLOWED.getMessage());
        }
        userEntity.setActive(false);
        return Mapper.entityToDTO(userEntity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserEntityResponseDTO enableUserEntity(Long userEntityId){
        UserEntity userEntity = findByUserEntityById(userEntityId);
        userEntity.setActive(true);
        return Mapper.entityToDTO(userEntity);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void resetUserEntityPassword(Long userEntityId, UserEntityResetPasswordDTO userEntityResetPasswordDTO){
        UserEntity userEntity = findByUserEntityById(userEntityId);
        if(userEntity.getUsername().equals("root")){
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.USER_ROOT_CHANGES_NOT_ALLOWED.getMessage());
        }
        verifyPasswordSecurity(userEntityResetPasswordDTO.newPassword());
        userEntity.setPassword(passwordEncoder.encode(userEntityResetPasswordDTO.newPassword()));
        userEntityRepository.save(userEntity);
    }

    @PreAuthorize("isAuthenticated()")
    public void updateUserEntityPassword(UserEntityPasswordRequestDTO userEntityPasswordRequestDTO){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = findByUserEntityByUsername(username);

        if(!passwordEncoder.matches(userEntityPasswordRequestDTO.oldPassword(), userEntity.getPassword()))
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.USER_PASSWORD_DONT_MATCH.getMessage());
        verifyPasswordSecurity(userEntityPasswordRequestDTO.newPassword());
        userEntity.setPassword(passwordEncoder.encode(userEntityPasswordRequestDTO.newPassword()));
        userEntityRepository.save(userEntity);
    }

    // Roles
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserEntityRoleDTO> getUserRoleList(){
        UserRoleEnum[] roles  =  UserRoleEnum.values();
        return Arrays.stream(roles).map(role ->
                new UserEntityRoleDTO(role.name(), role.getValue())).toList();
    }
    // Methods
    public UserEntity authenticateCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return  findByUserEntityByUsername(username);
    }

    public void verifyPasswordSecurity(String password){
        Pattern pattern = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.find()){
            throw new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_PASSWORD.getMessage());
        }
    }

    public UserEntity findByUserEntityByUsername(String username){
        return userEntityRepository.findByUsername(username).orElseThrow(
                () -> new AuthenticationException(HttpStatus.FORBIDDEN,AuthenticationExceptionEnum.NOT_REGISTERED_USER.getMessage()));
    }

    public UserEntity findByUserEntityById(Long userEntityId){
        return userEntityRepository.findById(userEntityId).orElseThrow(
                ()-> new ResourceNotFoundException(ResourceNotFoundExceptionEnum
                        .USER_NOT_FOUND.build(userEntityId)));
    }

    public void isUserEnabled(UserEntity user){
        if (!user.isActive()){
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.USER_DISABLED.getMessage());
        }
    }

    public void setDataToUpdatedEntity(UserEntity userEntity, UserEntityEditRequestDTO userEntityEditRequestDTO ){
        userEntity.setUsername(userEntityEditRequestDTO.username());
        userEntity.setFullName(userEntityEditRequestDTO.fullName());
        userEntity.setRole(UserRoleEnum.fromValue(userEntityEditRequestDTO.role())
                .orElseThrow(()-> new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_ROLE.getMessage())));
    }

    public void setDataToNewEntity(UserEntity userEntity, UserEntityDTO userEntityDTO){
        userEntity.setUsername(userEntityDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userEntityDTO.getPassword()));
        userEntity.setFullName(userEntityDTO.getFullName());
        userEntity.setRole(UserRoleEnum.fromValue(userEntityDTO.getRole())
                .orElseThrow(()-> new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_ROLE.getMessage())));
        userEntity.setActive(true);
    }
}


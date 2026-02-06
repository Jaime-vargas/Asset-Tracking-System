package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceFormatExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.enums.UserRoleEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceFormatException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.UserEntityDTO;
import com.control_activos.sks.control_activos.models.dto.UserEntityPasswordRequestDTO;
import com.control_activos.sks.control_activos.models.dto.UserEntityResponseDTO;
import com.control_activos.sks.control_activos.models.dto.UserEntityRoleDTO;
import com.control_activos.sks.control_activos.models.entity.UserEntity;
import com.control_activos.sks.control_activos.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;
    public UserEntityService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public List<UserEntityResponseDTO> getUserEntityDTOList (){
        List<UserEntity> userEntityList = userEntityRepository.findAll();
        return userEntityList.stream().map(Mapper::entityToDTO).toList();
    }

    public List<UserEntityRoleDTO> getUserRoleList(){
        UserRoleEnum[] roles  =  UserRoleEnum.values();
        return Arrays.stream(roles).map(role ->
                new UserEntityRoleDTO(role.name(), role.getValue())).toList();
    }
    @Transactional
    public UserEntityResponseDTO saveUserEntity(UserEntityDTO userEntityDTO){
        UserEntity userEntity = new UserEntity();
        setDataToNewEntity(userEntity, userEntityDTO);
        userEntity = userEntityRepository.save(userEntity);
        return Mapper.entityToDTO(userEntity);
    }
    @Transactional
    public UserEntityResponseDTO updateUserEntity(Long userEntityId, UserEntityResponseDTO userEntityResponseDTO){
        UserEntity userEntity = findByUserEntityId(userEntityId);
        setDataToUpdatedEntity(userEntity, userEntityResponseDTO);
        userEntity = userEntityRepository.save(userEntity);
        return Mapper.entityToDTO(userEntity);
    }
    // #TODO check implementation after apply bcrypt
    // #TODO implement enum Errors for password
    public void updateUserEntityPassword(Long userEntityId, UserEntityPasswordRequestDTO userEntityPasswordRequestDTO){
        UserEntity userEntity = findByUserEntityId(userEntityId);
        if(!userEntity.getPassword().equals(userEntityPasswordRequestDTO.oldPassword())){
            throw new OperationNotAllowedException(OperationNotAllowedExceptionEnum.USER_PASSWORD_DONT_MATCH.getMessage());
        }
        userEntity.setPassword(userEntityPasswordRequestDTO.newPassword());
        userEntityRepository.save(userEntity);
    }

    public UserEntity findByUserEntityId (Long userEntityId){
        return userEntityRepository.findById(userEntityId).orElseThrow(
                ()-> new ResourceNotFoundException(ResourceNotFoundExceptionEnum
                        .USER_NOT_FOUND.build(userEntityId)));
    }

    public void setDataToUpdatedEntity(UserEntity userEntity, UserEntityResponseDTO userEntityResponseDTO ){
        userEntity.setUsername(userEntityResponseDTO.getUsername());
        userEntity.setFullName(userEntityResponseDTO.getFullName());
        userEntity.setRole(UserRoleEnum.fromValue(userEntityResponseDTO.getRole())
                .orElseThrow(()-> new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_ROLE.getMessage())));
    }

    public void setDataToNewEntity(UserEntity userEntity, UserEntityDTO userEntityDTO){
        userEntity.setUsername(userEntityDTO.getUsername());
        userEntity.setPassword(userEntityDTO.getPassword());
        userEntity.setFullName(userEntityDTO.getFullName());
        userEntity.setRole(UserRoleEnum.fromValue(userEntityDTO.getRole())
                .orElseThrow(()-> new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_ROLE.getMessage())));
    }

    // #TODO Implements validation for RoleEnum and password format
}


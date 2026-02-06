package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.OperationNotAllowedExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.SucursalDTO;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.models.entity.Sucursal;
import com.control_activos.sks.control_activos.repository.SucursalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SucursalService {

    private final ClientService clientService;
    private final SucursalRepository sucursalRepository;
    public SucursalService(ClientService clientService, SucursalRepository sucursalRepository) {
        this.clientService = clientService;
        this.sucursalRepository = sucursalRepository;
    }
    @Transactional
    public SucursalDTO saveSucursal(Long clientId, SucursalDTO sucursalDTO) {
        Client client = clientService.findClientById(clientId);
        Sucursal sucursal = new Sucursal();
        sucursal.setClient(client);
        sucursal.setName(sucursalDTO.getName());
        sucursal = sucursalRepository.save(sucursal);
        return Mapper.entityToDTO(sucursal);
    }

    @Transactional
    public SucursalDTO editSucursal(Long clientId, Long sucursalId, SucursalDTO sucursalDTO) {
        Sucursal sucursal = findSucursalById(sucursalId);
        if (!sucursal.getClient().getId().equals(clientId)) {
            throw new OperationNotAllowedException(
                    OperationNotAllowedExceptionEnum.SUCURSAL_NOT_BELONG_TO_CLIENT.getMessage());
        }
        sucursal.setName(sucursalDTO.getName());
        sucursal = sucursalRepository.save(sucursal);
        return Mapper.entityToDTO(sucursal);
    }

    public Sucursal findSucursalById(Long sucursalId) {
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.SUCURSAL_NOT_FOUND.build(sucursalId)));
    }

}

package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.exception.NotFoundResourceException;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.SucursalDTO;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.models.entity.Sucursal;
import com.control_activos.sks.control_activos.repository.SucursalRepository;
import org.springframework.stereotype.Service;

@Service
public class SucursalService {

    private final ClientService clientService;
    private final SucursalRepository sucursalRepository;
    public SucursalService(ClientService clientService, SucursalRepository sucursalRepository) {
        this.clientService = clientService;
        this.sucursalRepository = sucursalRepository;
    }

    public SucursalDTO saveSucursal(Long clientId, SucursalDTO sucursalDTO) {
        Client client = clientService.findClientById(clientId);
        Sucursal sucursal = new Sucursal();
        sucursal.setClient(client);
        sucursal.setName(sucursalDTO.getName());
        sucursal = sucursalRepository.save(sucursal);
        return Mapper.entityToDTO(sucursal);
    }

    public SucursalDTO editSucursal(Long clientId, Long sucursalId, SucursalDTO sucursalDTO) {
        Sucursal sucursal = findSucursalById(sucursalId);
        if (!sucursal.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Sucursal does not belong to the specified client"); // #TODO: Custom Exception
        }
        sucursal.setName(sucursalDTO.getName());
        sucursal = sucursalRepository.save(sucursal);
        return Mapper.entityToDTO(sucursal);
    }

    public Sucursal findSucursalById(Long sucursalId) {
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new NotFoundResourceException("Sucursal not found")); // #TODO: Custom Exception with Enum
    }

}

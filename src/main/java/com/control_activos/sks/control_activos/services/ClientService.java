package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.models.dto.ClientDTO;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientDTO> getClientDTOList() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(Mapper::entityToDTO).toList();
    }

    @Transactional
    public ClientDTO saveClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        Client savedClient = clientRepository.save(client);
        return Mapper.entityToDTO(savedClient);
    }

    @Transactional
    public ClientDTO editClient(Long clientId, ClientDTO clientDTO) {
        Client client = findClientById(clientId);
        client.setName(clientDTO.getName());
        Client updatedClient = clientRepository.save(client);
        return Mapper.entityToDTO(updatedClient);
    }

    public Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.CLIENT_NOT_FOUND.build(clientId)));
    }

}

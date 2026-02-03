package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.models.dto.ClientDTO;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // --- Crud Methods for Client Entity ---

    public List<ClientDTO> getClientDTOList() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream().map(Mapper::entityToDTO).toList();
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        Client savedClient = clientRepository.save(client);
        return Mapper.entityToDTO(savedClient);
    }

    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found")); // #TODO: Custom Exception
        client.setName(clientDTO.getName());
        Client updatedClient = clientRepository.save(client);
        return Mapper.entityToDTO(updatedClient);
    }

    public Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

}

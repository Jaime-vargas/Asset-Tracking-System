package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.ResourceNotFoundExceptionEnum;
import com.control_activos.sks.control_activos.exception.ResourceNotFoundException;
import com.control_activos.sks.control_activos.mapper.BranchMapper;
import com.control_activos.sks.control_activos.mapper.ClientMapper;
import com.control_activos.sks.control_activos.mapper.PhotoMapper;
import com.control_activos.sks.control_activos.models.dto.BranchDTO;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.branchDTO.BranchTableDTO;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientDTO;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientTableDTO;
import com.control_activos.sks.control_activos.models.dto.clientDTO.ClientTableRowDTO;
import com.control_activos.sks.control_activos.models.dto.reportDTO.ReportCountDTO;
import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Client;
import com.control_activos.sks.control_activos.models.entity.Photo;
import com.control_activos.sks.control_activos.repository.BranchRepository;
import com.control_activos.sks.control_activos.repository.ClientRepository;
import com.control_activos.sks.control_activos.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final BranchRepository branchRepository;
    private final ClientRepository clientRepository;
    private final ReportRepository reportRepository;

    public ClientService( BranchRepository branchRepository, ClientRepository clientRepository, ReportRepository reportRepository) {
        this.branchRepository = branchRepository;
        this.clientRepository = clientRepository;
        this.reportRepository = reportRepository;
    }

    /** Client services */
    // GET ALL CLIENTS WITH ACTIVE REPORTS COUNT
    public List<ClientTableDTO> getAllClientTableDTO() {
        List<ClientTableRowDTO> clientRows = clientRepository.getClientTableRows();
        List<ReportCountDTO> allActiveReports = reportRepository.getAllActiveReportsGroupedByClientId();
        return MergeClientRowsAndActiveReportsToDTO(clientRows, allActiveReports);
    }

    // CREATE CLIENT
    @Transactional
    public ClientDTO saveClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        Client savedClient = clientRepository.save(client);
        return ClientMapper.toClientDTO(savedClient);
    }

    // UPDATE CLIENT
    @Transactional
    public ClientDTO editClient(Long clientId, ClientDTO clientDTO) {
        Client client = findClientById(clientId);
        client.setName(clientDTO.getName());
        Client updatedClient = clientRepository.save(client);
        return ClientMapper.toClientDTO(updatedClient);
    }

    /** Branch related services */
    // GET ALL BRANCHES OF A CLIENT WITH ACTIVE REPORTS COUNT
    public List<BranchTableDTO> getAllBranchTableDTOByClientId(Long clientId) {
        findClientById(clientId); // Validate client existence
        List<Branch> branchesById = branchRepository.findByClientId(clientId);
        List<ReportCountDTO> activeReportsById = reportRepository.findActiveReportsByClientId(clientId);
        return MergeBranchesAndActiveReportsToDTO(branchesById, activeReportsById);
    }



    /**********************************/

    // GET CLIENT WITH ACTIVE REPORTS COUNT
    public List<ClientTableDTO> getClientTableDTOByClientId(Long clientId) {
        List<ClientTableRowDTO>  clientTableDTO = clientRepository.getClientTableRowByClientId(clientId);
        List<ReportCountDTO> activeReportsById = reportRepository.getAllActiveReportsOfAClientByClientId(clientId);
        return MergeClientRowsAndActiveReportsToDTO(clientTableDTO, activeReportsById);
    }

    /**********************************/

    // EDIT CLIENT
    @Transactional
    public BranchDTO saveBranch(Long clientId, BranchDTO branchDTO) {
        Client client = findClientById(clientId);
        Branch branch = new Branch();
        branch.setName(branchDTO.getName());
        branch.setClient(client);
        Branch savedBranch = branchRepository.save(branch);
        return Mapper.entityToDTO(savedBranch);
    }

    /** Helper methods */
    private Map<Long, List<ReportCountDTO>> groupReportsById(List<ReportCountDTO> activeReports) {
        return activeReports.stream().collect(Collectors.groupingBy(ReportCountDTO::getId));
    }

    private List<BranchTableDTO> MergeBranchesAndActiveReportsToDTO(List<Branch> branches, List<ReportCountDTO> activeReports) {
        Map<Long, List<ReportCountDTO>> reportsByBranchId = groupReportsById(activeReports);
        return branches.stream().map(branch -> {
            BranchTableDTO branchTableDto = BranchMapper.toBranchTableDTO(branch);
            branchTableDto.setReportsActive(reportsByBranchId.getOrDefault(branch.getId(), List.of()));
            return branchTableDto;
        }).toList();
    }

    private List<ClientTableDTO> MergeClientRowsAndActiveReportsToDTO(List<ClientTableRowDTO> clientRows, List<ReportCountDTO> allActiveReports) {
        Map<Long, List<ReportCountDTO>> reportsByClientId = groupReportsById(allActiveReports);
        return clientRows.stream().map(row -> new ClientTableDTO(
                row.getId(),
                row.getName(),
                row.getBranches(),
                row.getTotalHardware(),
                reportsByClientId.getOrDefault(row.getId(), List.of()),
                PhotoMapper.toPhotoDTO(Optional.ofNullable(row.getPhoto()).orElse(new Photo()))
        )).toList();
    }

    /** Validations */
    public Client findClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ResourceNotFoundExceptionEnum.CLIENT_NOT_FOUND.build(clientId)));
    }

}

package com.control_activos.sks.control_activos.services;

import com.control_activos.sks.control_activos.enums.CameraImportColumns;
import com.control_activos.sks.control_activos.enums.ImportServiceExceptionEnum;
import com.control_activos.sks.control_activos.enums.ResourceFormatExceptionEnum;
import com.control_activos.sks.control_activos.exception.ImportServiceException;
import com.control_activos.sks.control_activos.exception.OperationNotAllowedException;
import com.control_activos.sks.control_activos.exception.ResourceFormatException;
import com.control_activos.sks.control_activos.mapper.CameraMapper;
import com.control_activos.sks.control_activos.models.dto.ImportResponse;
import com.control_activos.sks.control_activos.models.dto.cameraDTO.CameraEditRequestDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class BranchImportService {

    private final BranchService branchService;
    private final CameraService cameraService;

    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public byte[] generateImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Camera Import Template");
            Row header = sheet.createRow(0);
            for(int i = 0; i < CameraImportColumns.values().length; i++){
                header.createCell(i).setCellValue(CameraImportColumns.values()[i].getHeader());
                sheet.setColumnWidth(i, CameraImportColumns.values()[i].getCellWidth() * 256);
            }
            ByteArrayOutputStream template = new ByteArrayOutputStream();
            workbook.write(template);
            workbook.close();
            return template.toByteArray();
        }catch ( IOException e){
            throw new ImportServiceException(ImportServiceExceptionEnum.ERROR_GENERATING_TEMPLATE.getMessage(e.getMessage()));
        }
    }

    public ImportResponse importCamerasFromXLSX(Long branchId, MultipartFile file) {
        if (!CONTENT_TYPE_XLSX.equals(file.getContentType()))
            throw new ResourceFormatException(ResourceFormatExceptionEnum.INVALID_FILE_TYPE.getMessage());

        DataFormatter formatter = new DataFormatter();
        ImportResponse importResponse = new ImportResponse();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            branchService.findBranchById(branchId);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (isRowEmpty(row, formatter)) break;
                importResponse.processed();
                try {
                    Long cameraId = parseCameraId(formatter.formatCellValue(row.getCell(0)));
                    CameraEditRequestDTO cameraEditRequestDTO = CameraMapper.toCameraEditRequestDTO(row, formatter);

                    if(cameraId != null) {
                        cameraService.existsByIdAndBranchId(cameraId, branchId);
                        cameraService.updateCamera(cameraId, cameraEditRequestDTO);
                    }
                    else cameraService.saveCamera(branchId, cameraEditRequestDTO);
                    importResponse.successful();
                } catch (ResourceFormatException | OperationNotAllowedException | NumberFormatException e) {
                    importResponse.error("Error on row: " + (row.getRowNum() + 1) + " " + e.getMessage());
                }
            }
            System.out.println("processed: "+ importResponse.getProcessed());
            return importResponse;
        } catch (IOException e) {
            throw new ImportServiceException(ImportServiceExceptionEnum.ERROR_IMPORTING_FILE.getMessage(e.getMessage()));
        }
    }

    private Long parseCameraId(String idValue) throws NumberFormatException{
        if (idValue == null || idValue.isBlank()) return null;
        return Long.parseLong(idValue);
    }

    private boolean isRowEmpty(Row row, DataFormatter formatter){
        for (Cell cell : row) {
            if (!formatter.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}

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
import com.control_activos.sks.control_activos.models.entity.Camera;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BranchXLSService {

    private final BranchService branchService;
    private final CameraService cameraService;

    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public byte[] generateImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Camera Import Template");
            generateCameraHeaderRow(sheet);
            ByteArrayOutputStream template = new ByteArrayOutputStream();
            workbook.write(template);
            workbook.close();
            return template.toByteArray();
        }catch ( IOException e){
            throw new ImportServiceException(ImportServiceExceptionEnum.ERROR_GENERATING_TEMPLATE.getMessage(e.getMessage()));
        }
    }

    public byte[] exportCamerasToXLS(Long branchId){
        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Cameras");
            generateCameraHeaderRow(sheet);

            List<Camera> cameras = cameraService.getCamerasByBranchId(branchId);
            for(Camera camera : cameras){
                Row row = sheet.createRow(cameras.indexOf(camera) + 1);
                fillRowWithCameraData(row, camera);
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
            return importResponse;
        } catch (IOException e) {
            throw new ImportServiceException(ImportServiceExceptionEnum.ERROR_IMPORTING_FILE.getMessage(e.getMessage()));
        }
    }

    private void fillRowWithCameraData(Row row, Camera camera) {
        row.createCell(CameraImportColumns.ID.getIndex()).setCellValue(camera.getId());
        row.createCell(CameraImportColumns.CAMERA_ID.getIndex()).setCellValue(camera.getCameraId());
        row.createCell(CameraImportColumns.NAME.getIndex()).setCellValue(camera.getName());
        row.createCell(CameraImportColumns.BRAND.getIndex()).setCellValue(camera.getBrand());
        row.createCell(CameraImportColumns.MODEL.getIndex()).setCellValue(camera.getModel());
        row.createCell(CameraImportColumns.SERIAL_NUMBER.getIndex()).setCellValue(camera.getSerialNumber());
        row.createCell(CameraImportColumns.LOCATION.getIndex()).setCellValue(camera.getLocation());
        row.createCell(CameraImportColumns.MAC_ADDRESS.getIndex()).setCellValue(camera.getMacAddress());
        row.createCell(CameraImportColumns.IP_ADDRESS.getIndex()).setCellValue(camera.getIpAddress());
        row.createCell(CameraImportColumns.IDF.getIndex()).setCellValue(camera.getIdf());
        row.createCell(CameraImportColumns.USERNAME.getIndex()).setCellValue(camera.getUsername());
        row.createCell(CameraImportColumns.PASSWORD.getIndex()).setCellValue(camera.getPassword());
    }

    private void generateCameraHeaderRow(Sheet sheet) {
        Row header = sheet.createRow(0);
        for(int i = 0; i < CameraImportColumns.values().length; i++){
            header.createCell(i).setCellValue(CameraImportColumns.values()[i].getHeader());
            sheet.setColumnWidth(i, CameraImportColumns.values()[i].getCellWidth() * 256);
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

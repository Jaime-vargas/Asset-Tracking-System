package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.repository.BranchRepository;
import com.control_activos.sks.control_activos.repository.CameraRepository;
import com.control_activos.sks.control_activos.services.CameraService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RestController
public class MemoriaController {

    @Autowired
    CameraRepository cameraRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    CameraService cameraService;
    @Autowired
    SpringTemplateEngine templateEngine;

    /**
     * ALL METHODS IN THIS CONTROLLER MUST BE REFACTORED TO USE THE SERVICE LAYER, AND MUST BE OPTIMIZED TO AVOID REPEATED CODE AND ORGANIZED
     *
     */

    @GetMapping("{cameraId}/photoReportByCameraID")
    public ResponseEntity<byte[]> photoReportByCameraId (@PathVariable Long cameraId) {

        byte[] pdf = generatePhotoReportByCameraId(cameraId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"report.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);

    };

    @GetMapping("{branchId}/photoReport")
    public ResponseEntity<byte[]> photoReport (@PathVariable Long branchId) {


        byte[] pdf = generatePhotoReportByBranchId(branchId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"report.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);

    };

    @GetMapping("{branchId}/technicalMemory")
    public ResponseEntity<byte[]> technicalMemory (@PathVariable Long branchId) {


        byte[] pdf = generateTechnicalMemoryPdf(branchId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"report.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);

    };

    public byte[] generateTechnicalMemoryPdf(Long branchId) {
        List<Camera> cameras = cameraRepository.findByBranchId(branchId);

        List<Camera> cameras1 = new ArrayList<>(cameras);
        cameras1.addAll(cameras);
        cameras1.addAll(cameras);
        cameras1.addAll(cameras);
        cameras1.addAll(cameras);

        Branch branch = branchRepository.findById(branchId).get();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                Locale.of("es", "MX"));
        String actualDate = now.format(formatter);

        Context context = new Context();
        context.setVariable("branch", branch);
        context.setVariable("cameras", cameras1);
        context.setVariable("actualDate", actualDate);

        String html = templateEngine.process("technicalMemory", context);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            String baseUrl = new File(".")
                    .toURI()
                    .toString();
            builder.withHtmlContent(html, baseUrl);

            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

    }

    public byte[] generatePhotoReportByCameraId(Long cameraId) {

        Camera camera = cameraService.findCameraById(cameraId);
        Branch branch = camera.getBranch();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                Locale.of("es", "MX"));
        String actualDate = now.format(formatter);

        Context context = new Context();
        context.setVariable("branch", branch);
        context.setVariable("cameras", List.of(camera));
        context.setVariable("actualDate", actualDate);

        String html = templateEngine.process("PhotoReport", context);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            String baseUrl = new File(".")
                    .toURI()
                    .toString();
            builder.withHtmlContent(html, baseUrl);

            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public byte[] generatePhotoReportByBranchId(Long branchId) {

        List<Camera> cameras = cameraRepository.findByBranchId(branchId);

        Branch branch = branchRepository.findById(branchId).get();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                Locale.of("es", "MX"));
        String actualDate = now.format(formatter);


        Context context = new Context();
        context.setVariable("branch", branch);
        context.setVariable("cameras", cameras);
        context.setVariable("actualDate", actualDate);

        String html = templateEngine.process("PhotoReport", context);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();

            String baseUrl = new File(".")
                    .toURI()
                    .toString();
            builder.withHtmlContent(html, baseUrl);

            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    /**
     * ALL METHODS IN THIS CONTROLLER MUST BE REFACTORED TO USE THE SERVICE LAYER, AND MUST BE OPTIMIZED TO AVOID REPEATED CODE AND ORGANIZED
     *
     */

}

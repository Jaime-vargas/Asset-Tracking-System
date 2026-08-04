package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.repository.BranchRepository;
import com.control_activos.sks.control_activos.repository.CameraRepository;
import com.control_activos.sks.control_activos.services.CameraService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1")
public class MemoriaController {

    @Autowired
    CameraRepository cameraRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    CameraService cameraService;
    @Autowired
    SpringTemplateEngine templateEngine;

    /** TODO: ALL METHODS IN THIS CONTROLLER MUST BE REFACTORED TO USE THE SERVICE LAYER, AND MUST BE OPTIMIZED TO AVOID REPEATED CODE AND ORGANIZED
     *
     */

    @Value("${app.storage.base-path}")
    private String storagePath;

    @GetMapping("{cameraId}/photoReportByCameraID")
    public ResponseEntity<byte[]> photoReportByCameraId (@PathVariable Long cameraId) {

        byte[] pdf = generatePhotoReportByCameraId(cameraId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"photoReport.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);

    };

    @GetMapping("{branchId}/photoReport")
    public ResponseEntity<byte[]> photoReport (@PathVariable Long branchId) {


        byte[] pdf = generatePhotoReportByBranchId(branchId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"photoReport.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);

    };

    @GetMapping("{branchId}/technicalMemory")
    public ResponseEntity<byte[]> technicalMemory (@PathVariable Long branchId) {

        byte[] pdf = generateTechnicalMemoryPdf(branchId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"technicalMemory.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);

    };

    public byte[] generateTechnicalMemoryPdf(Long branchId) {

        String cssContent = null;
        String logo = null;
        String calendarIcon = null;
        String documentIcon = null;
        try {
            ClassPathResource css = new ClassPathResource("static/css/technicalMemory.css");
            cssContent = css.getContentAsString(java.nio.charset.StandardCharsets.UTF_8);
            logo = getImageAsBase64("static/assets/logo.png");
            calendarIcon = getImageAsBase64("static/assets/calendar.png");
            documentIcon = getImageAsBase64("static/assets/doc.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Camera> cameras = cameraRepository.findByBranchId(branchId);

        Branch branch = branchRepository.findById(branchId).get();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                Locale.of("es", "MX"));
        String actualDate = now.format(formatter);

        Context context = new Context();
        // Set the CSS content as a variable in the context
        context.setVariable("embeddedCss", cssContent);

        context.setVariable("logo", logo);
        context.setVariable("calendarIcon", calendarIcon);
        context.setVariable("documentIcon", documentIcon);

        context.setVariable("branch", branch);
        context.setVariable("cameras", cameras);
        context.setVariable("actualDate", actualDate);

        Path basepath = Path.of(storagePath);
        context.setVariable("storagePath", basepath.toUri().toString());

        String html = templateEngine.process("technicalMemory", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

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

        String cssContent = null;
        String logo = null;
        String calendarIcon = null;
        String documentIcon = null;
        try {
            ClassPathResource css = new ClassPathResource("static/css/photoReport.css");
            cssContent = css.getContentAsString(java.nio.charset.StandardCharsets.UTF_8);
            logo = getImageAsBase64("static/assets/logo.png");
            calendarIcon = getImageAsBase64("static/assets/calendar.png");
            documentIcon = getImageAsBase64("static/assets/doc.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Context context = new Context();

        context.setVariable("embeddedCss", cssContent);

        context.setVariable("logo", logo);
        context.setVariable("calendarIcon", calendarIcon);
        context.setVariable("documentIcon", documentIcon);

        context.setVariable("branch", branch);
        context.setVariable("cameras", List.of(camera));
        context.setVariable("actualDate", actualDate);

        Path basepath = Path.of(storagePath);
        context.setVariable("storagePath", basepath.toUri().toString());

        String html = templateEngine.process("photoReport", context);

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

        Branch branch = branchRepository.findById(branchId).get();

        List<Camera> cameras = cameraRepository.findByBranchId(branchId);


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",
                Locale.of("es", "MX"));
        String actualDate = now.format(formatter);

        String cssContent = null;
        String logo = null;
        String calendarIcon = null;
        String documentIcon = null;
        try {
            ClassPathResource css = new ClassPathResource("static/css/photoReport.css");
            cssContent = css.getContentAsString(java.nio.charset.StandardCharsets.UTF_8);
            logo = getImageAsBase64("static/assets/logo.png");
            calendarIcon = getImageAsBase64("static/assets/calendar.png");
            documentIcon = getImageAsBase64("static/assets/doc.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

        Context context = new Context();

        context.setVariable("embeddedCss", cssContent);

        context.setVariable("logo", logo);
        context.setVariable("calendarIcon", calendarIcon);
        context.setVariable("documentIcon", documentIcon);

        context.setVariable("branch", branch);
        context.setVariable("cameras", cameras);
        context.setVariable("actualDate", actualDate);

        Path basepath = Path.of(storagePath);
        context.setVariable("storagePath", basepath.toUri().toString());

        String html = templateEngine.process("photoReport", context);

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

    private String getImageAsBase64(String resourcePath) throws IOException {

            ClassPathResource resource = new ClassPathResource(resourcePath);
            byte[] bytes = resource.getInputStream().readAllBytes();
            String extension = resourcePath.substring(resourcePath.lastIndexOf('.') + 1);

            return "data:image/" + extension + ";base64," +
                    Base64.getEncoder().encodeToString(bytes);

    }

    /**
     * TODO: ALL METHODS IN THIS CONTROLLER MUST BE REFACTORED TO USE THE SERVICE LAYER, AND MUST BE OPTIMIZED TO AVOID REPEATED CODE AND ORGANIZED
     *
     */

}

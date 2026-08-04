package com.control_activos.sks.control_activos.Jwt;

import com.control_activos.sks.control_activos.mapper.CameraMapper;
import com.control_activos.sks.control_activos.mapper.HardwareMapper;
import com.control_activos.sks.control_activos.mapper.Mapper;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.CameraDetailDTO;
import com.control_activos.sks.control_activos.models.dto.hardwareDTO.HardwareDetailDTO;
import com.control_activos.sks.control_activos.models.entity.Branch;
import com.control_activos.sks.control_activos.models.entity.Camera;
import com.control_activos.sks.control_activos.models.entity.Hardware;
import com.control_activos.sks.control_activos.repository.BranchRepository;
import com.control_activos.sks.control_activos.repository.HardwareRepository;
import com.control_activos.sks.control_activos.services.BranchService;
import com.control_activos.sks.control_activos.services.CameraService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import io.nayuki.qrcodegen.QrCode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class TestingJwtController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    HardwareRepository hardwareRepository;

    @Autowired
    CameraService cameraService;

    @Autowired
    SpringTemplateEngine templateEngine;

    int scale = 10;

    // Endpoint to generate a token for a given hardware
    @GetMapping
    public ResponseEntity<String> getToken(@RequestParam Long id){
        return ResponseEntity.ok(jwtUtil.generateHardwareToken(id));
    }

    // Validate Tokens
    @GetMapping("/validate")
    public ResponseEntity<HardwareDetailDTO> validateToken(@RequestParam String token){
        Camera camera = cameraService.findCameraById(1L);
        return ResponseEntity.ok(HardwareMapper.hardwareDetailDTO(camera));
    }


    // Encoding qr codes
    public String generateQR(Long id) throws IOException {

        QrCode qrCode = QrCode.encodeText(jwtUtil.generateHardwareToken(id), QrCode.Ecc.MEDIUM);

        BufferedImage image = new BufferedImage(
                qrCode.size * 10,
                qrCode.size * 10,
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < qrCode.size; y++) {
            for (int x = 0; x < qrCode.size; x++) {
                boolean module = qrCode.getModule(x, y);
                int color = module
                        ? 0x000000
                        : 0xFFFFFF;
                // pintar bloque 10x10
                for (int dy = 0; dy < scale; dy++) {

                    for (int dx = 0; dx < scale; dx++) {

                        image.setRGB(
                                x * scale + dx,
                                y * scale + dy,
                                color
                        );
                    }
                }
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", outputStream);

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    @GetMapping("/qr/pdf/{branchId}")
    public ResponseEntity<byte[]> photoReport (@PathVariable Long branchId) throws IOException {

        byte[] pdf = generateQrPdf(branchId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qr.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);
    };



    public byte[] generateQrPdf(Long branchId) {


        List<Hardware> hardwareList = hardwareRepository.findHardwareByBranchId(branchId);

        int qrWidth = 30;
        final int qrPadding = 6;

        double pageWidth = 192.0;

        int columns =  (int) Math.floor(pageWidth / (qrWidth + qrPadding));

        int width = (int) (pageWidth / columns);


        Map<Long, String> qrCodes = new HashMap<>();

        hardwareList.forEach(c -> {
            try {
                qrCodes.put(c.getId(), generateQR(c.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        String cssContent = null;
        try {
            ClassPathResource css = new ClassPathResource("static/css/qrFormat.css");
            cssContent = css.getContentAsString(java.nio.charset.StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Context context = new Context();

        context.setVariable("embeddedCss", cssContent);

        context.setVariable("qr", qrCodes);
        context.setVariable("cameras", hardwareList);
        context.setVariable("width", qrWidth);
        context.setVariable("qrSize", width - 1);

        String html = templateEngine.process("qrFormat", context);

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
}

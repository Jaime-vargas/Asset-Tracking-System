package com.control_activos.sks.control_activos.controller;

import com.control_activos.sks.control_activos.services.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final FilesService filesService;

}

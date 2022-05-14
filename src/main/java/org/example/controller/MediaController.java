package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.MediaUploadResponseDTO;
import org.example.manager.MediaManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/media")
public class MediaController {
    private MediaManager manager;

    @RequestMapping("/upload-multipart")
    public MediaUploadResponseDTO uploadMultipart(MultipartFile file) {
        return new MediaUploadResponseDTO();
    }

    @RequestMapping("/upload-data")
    public MediaUploadResponseDTO uploadBytes(@RequestBody byte[] data) throws IOException {
        return manager.upload(data);
    }
}

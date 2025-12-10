package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads/chat/";

    public String save(MultipartFile file) {
        try {
            String original = Objects.requireNonNull(file.getOriginalFilename());
            String ext = "";
            int dot = original.lastIndexOf(".");
            if (dot != -1) {
                ext = original.substring(dot);
            }

            String filename = UUID.randomUUID() + ext;
            Path path = Paths.get(UPLOAD_DIR + filename);

            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            // URL public sẽ mapping qua WebMvc (StaticResourceConfig)
            // /files/chat/xxx.jpg
            return "/files/chat/" + filename;

        } catch (Exception e) {
            throw new RuntimeException("Cannot save file", e);
        }
    }
}

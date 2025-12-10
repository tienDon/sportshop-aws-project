package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;


import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "local")
public class LocalFileStorageService implements IFileStorageService {

    @Value("${upload.base-dir:uploads}")
    private String baseDir;

    @Override
    public String saveFile(MultipartFile file, String folder) {

        try {
            // Tạo folder theo ngày: uploads/chat/2025/02/20
            LocalDate now = LocalDate.now();
            String datePath = now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth();

            File uploadDir = new File(baseDir + "/" + folder + "/" + datePath);
            uploadDir.mkdirs();

            // Tạo tên file random
            String ext = file.getOriginalFilename().substring(
                    file.getOriginalFilename().lastIndexOf(".")
            );

            String fileName = UUID.randomUUID() + ext;

            File destination = new File(uploadDir, fileName);
            file.transferTo(destination);

            // Trả về URL để FE dùng hiển thị
            return "/uploads/" + folder + "/" + datePath + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Cannot save file", e);
        }
    }
}

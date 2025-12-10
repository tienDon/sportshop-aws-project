package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

    String saveFile(MultipartFile file, String folder); // trả về URL đầy đủ

    default String detectContentType(MultipartFile file) {
        return file.getContentType();
    }
}

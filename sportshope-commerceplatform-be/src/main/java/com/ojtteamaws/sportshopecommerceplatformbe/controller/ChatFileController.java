package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatFileController {

    private final IFileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {

        // 1) Lưu file qua abstraction (S3 hoặc local tùy storage.type)
        String url = fileStorageService.saveFile(file, "chat");

        // 2) Lấy MIME thật từ file, rồi map sang IMAGE/VIDEO/FILE
        String mime = file.getContentType();
        String type = resolveType(mime);

        // 3) Trả về đúng format FE đang dùng: { url, contentType }
        return ResponseEntity.ok(
                Map.of(
                        "url", url,
                        "contentType", type
                )
        );
    }

    private String resolveType(String mime) {
        if (mime == null) return "FILE";

        if (mime.startsWith("image")) return "IMAGE";
        if (mime.startsWith("video")) return "VIDEO";
        return "FILE";
    }
}

package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class S3FileStorageService implements IFileStorageService {

    // S3 client (bước sau mình sẽ thêm config bean nếu bạn cần)
    private final S3Client s3Client;

    // Tên bucket, ví dụ: my-chat-app-bucket
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    // Base URL public của bucket, ví dụ:
    // https://my-chat-app-bucket.s3.ap-southeast-1.amazonaws.com
    @Value("${aws.s3.base-url}")
    private String baseUrl;

    @Override
    public String saveFile(MultipartFile file, String folder) {

        try {
            // Lấy extension
            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null) {
                int dot = originalName.lastIndexOf('.');
                if (dot != -1) {
                    ext = originalName.substring(dot);
                }
            }

            // Tạo path theo ngày: folder/yyyy/M/d/UUID.ext
            LocalDate now = LocalDate.now();
            String datePath =
                    now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth();

            String key = folder + "/" + datePath + "/" + UUID.randomUUID() + ext;

            // Tạo request upload lên S3
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            // Ghép full URL public trả về cho FE
            // baseUrl: https://bucket.s3.region.amazonaws.com (không có / ở cuối)
            String prefix = baseUrl.endsWith("/")
                    ? baseUrl.substring(0, baseUrl.length() - 1)
                    : baseUrl;

            return prefix + "/" + key;

        } catch (IOException e) {
            throw new RuntimeException("Cannot upload file to S3", e);
        }
    }
}

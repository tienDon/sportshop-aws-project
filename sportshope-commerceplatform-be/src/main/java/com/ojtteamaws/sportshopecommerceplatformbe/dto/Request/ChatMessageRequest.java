package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRequest {
    private String content;
    private String fileUrl;      // ảnh/video
    private String contentType;  // image/png, video/mp4..
}

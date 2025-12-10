package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String content;
    private LocalDateTime sentAt;
    private String fileUrl;
    private String contentType;
}

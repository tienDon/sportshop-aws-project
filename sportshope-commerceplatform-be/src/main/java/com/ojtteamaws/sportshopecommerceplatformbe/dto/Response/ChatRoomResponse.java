package com.ojtteamaws.sportshopecommerceplatformbe.dto.Response;

import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long adminId;
    private String adminName;
    private ChatRoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
}

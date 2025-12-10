package com.ojtteamaws.sportshopecommerceplatformbe.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatRoomRequest {
    // Có thể cho phép gán sẵn staff, hoặc để null cho admin assign sau
    private Long adminId;
}

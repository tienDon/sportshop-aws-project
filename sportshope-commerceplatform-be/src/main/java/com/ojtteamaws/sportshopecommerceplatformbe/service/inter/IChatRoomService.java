package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatRoomResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatRoom;

import java.util.List;

public interface IChatRoomService {

    // staffId → đổi tên adminId cho đồng nhất Controller
    ChatRoom createRoom(Long customerId, Long adminId);

    ChatRoom getById(Long roomId);

    List<ChatRoomResponse> getRoomsForCustomer(Long customerId);

    List<ChatRoomResponse> getRoomsForAdmin(Long adminId);
}

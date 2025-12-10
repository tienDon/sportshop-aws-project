package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CreateChatRoomRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatRoomResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatRoom;
import com.ojtteamaws.sportshopecommerceplatformbe.mapper.ChatRoomMapper;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final IChatRoomService roomService;
    private final ChatRoomMapper mapper;

    // Customer tạo room
    @PostMapping
    public ChatRoomResponse createRoom(
            @RequestBody CreateChatRoomRequest req,
            Principal principal
    ) {
        Long customerId = Long.parseLong(principal.getName());
        Long adminId = req.getAdminId();

        ChatRoom room = roomService.createRoom(customerId, adminId);
        return mapper.toResponse(room);
    }

    // Customer xem phòng của họ
    @GetMapping("/me")
    public List<ChatRoomResponse> myRooms(Principal principal) {
        Long customerId = Long.parseLong(principal.getName());
        return roomService.getRoomsForCustomer(customerId);
    }

    // Admin xem room OPEN
    @GetMapping("/admin/me")
    public List<ChatRoomResponse> myAdminRooms(Principal principal) {
        Long adminId = Long.parseLong(principal.getName());
        return roomService.getRoomsForAdmin(adminId);
    }
}

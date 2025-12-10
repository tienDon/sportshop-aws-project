package com.ojtteamaws.sportshopecommerceplatformbe.mapper;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.CreateChatRoomRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatRoomResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {

    @Mapping(target = "id", source = "id")

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")

    @Mapping(target = "adminId", source = "admin.id")
    @Mapping(target = "adminName", source = "admin.fullName")

    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "lastMessageAt", source = "lastMessageAt")
    ChatRoomResponse toResponse(ChatRoom room);

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "admin", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "lastMessageAt", ignore = true)
    ChatRoom fromCreateRequest(CreateChatRoomRequest request);
}

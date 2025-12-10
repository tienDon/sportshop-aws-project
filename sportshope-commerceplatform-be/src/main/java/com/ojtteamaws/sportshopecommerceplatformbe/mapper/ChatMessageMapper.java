package com.ojtteamaws.sportshopecommerceplatformbe.mapper;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ChatMessageRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatMessageResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    // -------- Request → Entity --------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)          // set trong service
    @Mapping(target = "senderId", ignore = true)      // lấy từ principal
    @Mapping(target = "sentAt", ignore = true)        // tự gán @PrePersist

    // ⭐ Map thêm 2 field fileUrl + contentType
    @Mapping(target = "fileUrl", source = "fileUrl")
    @Mapping(target = "contentType", source = "contentType")
    ChatMessage fromRequest(ChatMessageRequest request);


    // -------- Entity → Response --------
    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "senderId", source = "senderId")

    @Mapping(target = "content", source = "content")

    // ⭐ Map thêm 2 field mới
    @Mapping(target = "fileUrl", source = "fileUrl")
    @Mapping(target = "contentType", source = "contentType")

    @Mapping(target = "sentAt", source = "sentAt")
    ChatMessageResponse toResponse(ChatMessage message);

}

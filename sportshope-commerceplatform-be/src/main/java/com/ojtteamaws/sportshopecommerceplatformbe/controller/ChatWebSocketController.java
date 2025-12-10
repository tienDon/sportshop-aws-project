package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ChatMessageRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatMessageResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatMessage;
import com.ojtteamaws.sportshopecommerceplatformbe.mapper.ChatMessageMapper;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final IChatService chatService;
    private final ChatMessageMapper chatMessageMapper;

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public ChatMessageResponse sendMessage(@DestinationVariable Long roomId,
                                           @Payload ChatMessageRequest request,
                                           Principal principal) {

        Long senderId = Long.parseLong(principal.getName());

        ChatMessage saved = chatService.saveMessage(roomId, senderId, request);

        return chatMessageMapper.toResponse(saved);
    }
}

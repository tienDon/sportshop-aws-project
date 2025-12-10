package com.ojtteamaws.sportshopecommerceplatformbe.controller;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatMessageResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatMessage;
import com.ojtteamaws.sportshopecommerceplatformbe.mapper.ChatMessageMapper;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final IChatService chatService;
    private final ChatMessageMapper chatMessageMapper;

    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId) {

        List<ChatMessage> messages = chatService.getMessages(roomId);

        return messages.stream()
                .map(chatMessageMapper::toResponse)
                .collect(Collectors.toList());
    }
}

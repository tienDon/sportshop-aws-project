package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;
import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ChatMessageRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatMessage;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatRoom;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ChatMessageRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ChatRoomRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IChatRoomService;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final IChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatMessage saveMessage(Long roomId, Long senderId, ChatMessageRequest request) {
        ChatRoom room = chatRoomService.getById(roomId);

        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setSenderId(senderId);

        // TEXT
        message.setContent(request.getContent());

        // FILE / IMAGE / VIDEO (nếu có)
        message.setFileUrl(request.getFileUrl());
        message.setContentType(request.getContentType());
        ChatMessage saved = chatMessageRepository.save(message);
        room.setLastMessageAt(saved.getSentAt());
        chatRoomRepository.save(room);
        return saved;
    }

    @Override
    public List<ChatMessage> getMessages(Long roomId) {
        ChatRoom room = chatRoomService.getById(roomId);
        return chatMessageRepository.findByRoomOrderBySentAtAsc(room);
    }
}
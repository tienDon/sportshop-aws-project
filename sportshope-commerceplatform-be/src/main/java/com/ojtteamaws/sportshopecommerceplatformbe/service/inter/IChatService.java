package com.ojtteamaws.sportshopecommerceplatformbe.service.inter;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Request.ChatMessageRequest;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatMessage;

import java.util.List;

public interface IChatService {

    ChatMessage saveMessage(Long roomId, Long senderId, ChatMessageRequest request);

    List<ChatMessage> getMessages(Long roomId);
}

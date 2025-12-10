package com.ojtteamaws.sportshopecommerceplatformbe.service.impl;

import com.ojtteamaws.sportshopecommerceplatformbe.dto.Response.ChatRoomResponse;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatRoom;
import com.ojtteamaws.sportshopecommerceplatformbe.entity.User;
import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.ChatRoomStatus;
import com.ojtteamaws.sportshopecommerceplatformbe.mapper.ChatRoomMapper;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.ChatRoomRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.repository.IUserRepository;
import com.ojtteamaws.sportshopecommerceplatformbe.service.inter.IChatRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements IChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final IUserRepository userRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public synchronized ChatRoom createRoom(Long customerId, Long adminId) {

        ChatRoom existing = chatRoomRepository
                .findTopByCustomer_IdOrderByCreatedAtAsc(customerId)
                .orElse(null);

        if (existing != null) {
            if (existing.getStatus() == ChatRoomStatus.CLOSED) {
                existing.setStatus(ChatRoomStatus.OPEN);
                chatRoomRepository.save(existing);
            }
            return existing;
        }

        User customer = userRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User admin = userRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        ChatRoom room = new ChatRoom();
        room.setCustomer(customer);
        room.setAdmin(admin);
        room.setStatus(ChatRoomStatus.OPEN);

        room = chatRoomRepository.save(room);

        messagingTemplate.convertAndSend(
                "/topic/admin/new-room",
                chatRoomMapper.toResponse(room)
        );

        return room;
    }

    @Override
    public List<ChatRoomResponse> getRoomsForCustomer(Long customerId) {
        return chatRoomRepository
                .findByCustomer_IdOrderByLastMessageAtDesc(customerId)
                .stream()
                .map(chatRoomMapper::toResponse)
                .toList();
    }

    @Override
    public List<ChatRoomResponse> getRoomsForAdmin(Long adminId) {
        return chatRoomRepository
                .findByAdmin_IdAndStatusOrderByLastMessageAtDesc(adminId, ChatRoomStatus.OPEN)
                .stream()
                .map(chatRoomMapper::toResponse)
                .toList();
    }

    @Override
    public ChatRoom getById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
    }
}

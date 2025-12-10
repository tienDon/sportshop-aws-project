package com.ojtteamaws.sportshopecommerceplatformbe.repository;

import com.ojtteamaws.sportshopecommerceplatformbe.entity.ChatRoom;
import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findTopByCustomer_IdOrderByCreatedAtAsc(Long customerId);

    List<ChatRoom> findByCustomer_IdOrderByLastMessageAtDesc(Long customerId);

    List<ChatRoom> findByAdmin_IdAndStatusOrderByLastMessageAtDesc(
            Long adminId, ChatRoomStatus status
    );
}


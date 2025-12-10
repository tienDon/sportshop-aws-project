package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import com.ojtteamaws.sportshopecommerceplatformbe.enumEntity.ChatRoomStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------- FK Customer -----------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // ----------- FK Admin (staff cũ) -----------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus status = ChatRoomStatus.OPEN;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private LocalDateTime lastMessageAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lastMessageAt = this.createdAt;
    }
}

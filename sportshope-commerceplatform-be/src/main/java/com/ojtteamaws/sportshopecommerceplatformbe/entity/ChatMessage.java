package com.ojtteamaws.sportshopecommerceplatformbe.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    private Long senderId;

    private String content;

    private LocalDateTime sentAt;

    private String fileUrl;
    private String contentType;

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }
}

package com.rescue.rescue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.rescue.rescue.enums.NotificationStatus;

@Entity
@Table(name = "Notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    @Builder.Default
    private LocalDate createAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(name = "title",nullable = false)
    private String title;
}
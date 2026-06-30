package com.rescue.rescue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.rescue.rescue.enums.HistoryStatus;

@Entity
@Table(name = "hitories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rescue_team_id", nullable = false)
    private RescueTeam rescueTeam;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder.Default
    @Column(name = "create_at")         
    private LocalDate createAt = LocalDate.now();

    @Builder.Default
    @Column(name = "update_at")
    private LocalDate updateAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HistoryStatus status;
}
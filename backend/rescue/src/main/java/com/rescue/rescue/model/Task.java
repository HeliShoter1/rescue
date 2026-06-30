package com.rescue.rescue.model;

import java.time.LocalDate;

import com.rescue.rescue.enums.TaskStatus;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "rescue_team_id")
    private RescueTeam rescueTeam;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Nullable
    private User user;

    @Builder.Default
    private LocalDate createAt = LocalDate.now();

    @Builder.Default
    private LocalDate updateAt = LocalDate.now();
}

package com.rescue.rescue.model;

import java.util.List;

import com.rescue.rescue.enums.RescueTeamStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rescue_teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RescueTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private RescueTeamStatus status = RescueTeamStatus.AVAILABLE;

    @OneToMany(mappedBy = "rescueTeam")
    private List<Task> tasks;

}

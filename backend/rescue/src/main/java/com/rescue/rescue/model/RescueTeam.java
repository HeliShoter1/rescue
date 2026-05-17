package com.rescue.rescue.model;

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
    
    @OneToOne
    @JoinColumn(name = "victim_id", nullable = false)
    private User victim;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RescueTeamStatus status = RescueTeamStatus.AVAILABLE;

}

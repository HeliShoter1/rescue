package com.rescue.rescue.model;

import com.rescue.rescue.enums.MemberStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rescue_id", nullable = false)
    private RescueTeam rescueTeam;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private MemberStatus status = MemberStatus.PENDING;

    public Group orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}

package com.rescue.rescue.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import org.apache.ibatis.annotations.One;

@Entity
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longtude", nullable = false)  
    private Double longitude;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "place")
    private List<RescueTeam> rescueTeams;
}

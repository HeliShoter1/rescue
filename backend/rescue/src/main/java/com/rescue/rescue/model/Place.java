package com.rescue.rescue.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "place")
    private List<Post> posts;

    @OneToMany(mappedBy = "place")
    private List<RescueTeam> rescueTeams;
}

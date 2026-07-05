package com.rescue.rescue.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import org.apache.ibatis.annotations.One;

import com.rescue.rescue.enums.TypePlace;

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
    private Double longtude;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY)
    @Nullable
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_place", nullable = true)
    private TypePlace typePlace;

}

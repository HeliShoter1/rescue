package com.rescue.rescue.model;

import com.rescue.rescue.enums.RelationshipType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "relatives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Relative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent", nullable = false)
    private User relative;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipType relationship;
}

package com.rescue.rescue.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.rescue.rescue.enums.PostStatus;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "create_at")
    private LocalDate createAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status = PostStatus.PENDING;

    @Column(name = "update_at")
    private LocalDate updateAt = LocalDate.now();

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "id_place", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "post")
    private List<History> histories;
}

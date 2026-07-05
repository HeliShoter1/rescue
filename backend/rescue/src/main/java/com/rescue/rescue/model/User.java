package com.rescue.rescue.model;

import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.enums.UserRole;    import com.rescue.rescue.model.Message;
import com.rescue.rescue.model.Notification;
import com.rescue.rescue.model.Post;
import com.rescue.rescue.model.RescueTeam;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.One;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.SAFE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private UserRole role = UserRole.CITIZEN;

    @Column(name = "create_at")
    @Builder.Default
    private LocalDate createAt = LocalDate.now();

    @Column(name = "update_at")
    @Builder.Default
    private LocalDate updateAt = LocalDate.now();

    @Column(name = "password_changed_at")
    @Builder.Default
    private LocalDate passwordChangedAt = LocalDate.now();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_place")
    private Place place;

    @OneToMany(mappedBy = "user")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "sender")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "user")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Notification> notifications;

    @OneToMany(mappedBy = "sender")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Notification> sentNotifications;

    @OneToMany(mappedBy = "user")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Task> tasks;

}

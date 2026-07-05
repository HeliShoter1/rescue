package com.rescue.rescue.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.enums.PostStatus;
import com.rescue.rescue.enums.RescueTeamStatus;
import com.rescue.rescue.enums.TypePlace;
import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.model.Post;
import com.rescue.rescue.model.RescueTeam;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.GroupRepository;
import com.rescue.rescue.reponsitory.PlaceRepository;
import com.rescue.rescue.reponsitory.PostRepository;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserReponsitory userRepository;
    private final PlaceRepository placeRepository;
    private final PostRepository postRepository;
    private final RescueTeamRepository rescueTeamRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping seeder.");
            return;
        }

        System.out.println("Seeding database with test data...");

        // 1. Create Places
        Place citizenPlace = Place.builder()
                .name("Xóm 3, Xã Thạch Trung, TP. Hà Tĩnh")
                .latitude(18.3685)
                .longtude(105.8894)
                .typePlace(TypePlace.USER_LOCATION)
                .build();
        placeRepository.save(citizenPlace);

        Place managerPlace = Place.builder()
                .name("Văn phòng Cứu hộ Trung tâm, Hà Tĩnh")
                .latitude(18.3490)
                .longtude(105.9012)
                .typePlace(TypePlace.USER_LOCATION)
                .build();
        placeRepository.save(managerPlace);

        Place adminPlace = Place.builder()
                .name("Ủy ban Phòng chống Thiên tai, Hà Tĩnh")
                .latitude(18.3551)
                .longtude(105.9080)
                .typePlace(TypePlace.USER_LOCATION)
                .build();
        placeRepository.save(adminPlace);

        // 2. Create Users
        User citizen = User.builder()
                .name("Nguyễn Văn Dân")
                .phoneNumber("0901000001")
                .password(passwordEncoder.encode("123456"))
                .role(UserRole.CITIZEN)
                .status(UserStatus.SAFE)
                .place(citizenPlace)
                .build();
        userRepository.save(citizen);

        User manager = User.builder()
                .name("Phạm Văn Trưởng")
                .phoneNumber("0901000002")
                .password(passwordEncoder.encode("123456"))
                .role(UserRole.MANAGER)
                .status(UserStatus.SAFE)
                .place(managerPlace)
                .build();
        userRepository.save(manager);

        User admin = User.builder()
                .name("Đỗ Hoàng Admin")
                .phoneNumber("0901000099")
                .password(passwordEncoder.encode("123456"))
                .role(UserRole.ADMIN)
                .status(UserStatus.SAFE)
                .place(adminPlace)
                .build();
        userRepository.save(admin);

        // 3. Create a Post (SOS Request)
        Post post = Post.builder()
                .user(citizen)
                .content("[KHAN_CAP] [DI_TAN] [4 người] Nước dâng cao ngập nửa tầng 1, nhà có người già và trẻ nhỏ cần di tản khẩn cấp.")
                .status(PostStatus.PENDING)
                .createAt(LocalDate.now())
                .updateAt(LocalDate.now())
                .build();
        postRepository.save(post);

        // 4. Create a Rescue Team
        RescueTeam team = RescueTeam.builder()
                .post(post)
                .status(RescueTeamStatus.AVAILABLE)
                .build();
        rescueTeamRepository.save(team);

        // 5. Assign Manager to Rescue Team (Group membership)
        Group group = Group.builder()
                .user(manager)
                .rescueTeam(team)
                .status(MemberStatus.ACCEPTED)
                .build();
        groupRepository.save(group);

        System.out.println("Database seeding completed successfully!");
    }
}

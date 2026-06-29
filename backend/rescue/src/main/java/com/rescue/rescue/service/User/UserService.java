package com.rescue.rescue.service.User;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.dto.UserDto;
import com.rescue.rescue.enums.PostStatus;
import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.exceptions.UserAlreadyExistsException;
import com.rescue.rescue.exceptions.UserNotFoundException;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.model.Post;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.PlaceRepository;
import com.rescue.rescue.reponsitory.PostRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.request.CreatePlace;
import com.rescue.rescue.request.CreateUserRequest;
import com.rescue.rescue.request.UserUpdatePassword;
import com.rescue.rescue.request.UserUpdateRole;
import com.rescue.rescue.request.UserUpdateStatus;
import com.rescue.rescue.sercurity.user.RescueUserDetail;
import com.rescue.rescue.service.Notification.NotificationService;
import com.rescue.rescue.service.Place.PlaceService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {
    private final UserReponsitory userRepository;
    private final ModelMapper modelMapper;
    private final PlaceRepository placeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Override
    public List<UserDto> getAllUsers(UserStatus status, UserRole role, String search, Long cursor, Integer limit) {
        List<User> users = userRepository.findByFilter(
            status != null ? status : null,
            role != null ? role : null,
            search != null ? search : null,
            cursor != null ? cursor : 0L,
            limit != null ? limit : 10
        );
        return users.stream().map(this::convertDto).toList();
    }


    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return this.convertDto(user);
    }

    @Override
    public UserDto getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("User not found with phone number: " + phoneNumber));
        return this.convertDto(user);
    }

    @Override
    public UserDto createUser(CreateUserRequest userRequest){
        if (userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with phone number: " + userRequest.getPhoneNumber());
        }
        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User savedUser = userRepository.save(user);
        return this.convertDto(savedUser);
    }

    @Override
    public UserDto updateUserStatus(UserUpdateStatus userUpdateStatus) {
        Authentication authentication ;
        Long id;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            id = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        user.setStatus(userUpdateStatus.getStatus());
        userRepository.updateStatusById(id, userUpdateStatus.getStatus());
        Post post = Post.builder()
                .user(user)
                .content("User " + user.getName() + " has changed status to " + userUpdateStatus.getStatus())
                .status(PostStatus.PENDING)
                .build();
        postRepository.save(post);
        if(user.getStatus().equals(UserStatus.EMERGENCY)){
            List<User> admin = userRepository.findByRole(UserRole.ADMIN);
            for(User u: admin){
                notificationService.sendViaQueue(u.getId(), id, "User EMERGENCY", "User " + user.getName() + " has status to EMERGENCY");
            }
        }
        return this.convertDto(user);
    }
    @Override 
    public UserDto updateUserPassword(UserUpdatePassword userUpdatePassword) {
        Authentication authentication ;
        Long id;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            id = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        if (!passwordEncoder.matches(userUpdatePassword.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(userUpdatePassword.getNewPassword()));
        User updatedUser = userRepository.save(user);
        return this.convertDto(updatedUser);
    }

    @Override
    public UserDto updateUserRole(Long userId, UserUpdateRole newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setRole(newRole.getRole());
        User updatedUser = userRepository.save(user);
        return this.convertDto(updatedUser);
    }

    @Override
    public UserDto updateUserPlace(CreatePlace createPlace) {
        Authentication authentication ;
        Long id;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            id = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        Place place = Place.builder()
                .name(createPlace.getName())
                .latitude(createPlace.getLatitude())
                .longtude(createPlace.getLongtude())
                .build();
        placeRepository.save(place);
        userRepository.updatePlaceById(id, place);
        return this.convertDto( userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id)));
    }

    @Override
    public UserDto convertDto(User user) {
        return UserDto.fromEntity(user);
    }
}

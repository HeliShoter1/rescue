package com.rescue.rescue.service.Relative;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.RelativeDto;
import com.rescue.rescue.enums.RelationshipType;
import com.rescue.rescue.model.Relative;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.RelativeReponsitory;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.request.CreateRelative;
import com.rescue.rescue.sercurity.user.RescueUserDetail;
import com.rescue.rescue.service.Notification.INotificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RelativeService implements IRelativeService {
    private final ModelMapper modelMapper;
    private final RelativeReponsitory relativeRepository;
    private final UserReponsitory userRepository;
    private final INotificationService notificationService;

    @Override
    public List<RelativeDto> getRelativesByUserId(Long cursor, Integer limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        return relativeRepository.findByUserId(userId, cursor, limit).stream()
                .map(RelativeDto::fromEntity)
                .toList();
    }

   @Override
    public RelativeDto createRelative(CreateRelative relativeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User relativeUser = userRepository.findById(relativeDto.getRelativeId())
                .orElseThrow(() -> new UsernameNotFoundException("Relative user not found"));

        Relative relative = Relative.builder()
                .user(user)
                .relative(relativeUser)
                .relationship(RelationshipType.valueOf(relativeDto.getRelationship()))
                .build();

        RelativeDto saved = RelativeDto.fromEntity(relativeRepository.save(relative));

        notificationService.sendNotification(
                relative.getRelative().getId(),
                "Thêm người thân",
                "Bạn vừa được thêm vào danh sách người thân"
        );

        return saved;
    }

    @Override
    public void deleteRelative(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        Relative relative = relativeRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Relative not found with id: " + id));
        if (!relative.getUser().getId().equals(userId) && !relative.getRelative().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to delete this relative");
        }
        relativeRepository.deleteById(id);
        notificationService.sendNotification(
                relative.getRelative().getId(),
                "Xóa người thân",
                "Bạn vừa bị xóa khỏi danh sách người thân"
        );
    }    
}

package com.rescue.rescue.service.Relative;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.RelativeDto;
import com.rescue.rescue.model.Relative;
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
                .map(relative -> modelMapper.map(relative, RelativeDto.class))
                .toList();
    }

    @Override
public RelativeDto createRelative(CreateRelative relativeDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
    
    Relative relative = modelMapper.map(relativeDto, Relative.class);
    relative.setUser(userRepository.findById(userId).orElse(null));
    RelativeDto saved = modelMapper.map(relativeRepository.save(relative), RelativeDto.class);

    // Push notification đến người được thêm vào
    notificationService.sendNotification(
            relativeDto.getRelativeId(),
            "Thêm người thân",
            "Bạn vừa được thêm vào danh sách người thân"
    );

    return saved;
}

    @Override
    public void deleteRelative(Long id) {
        Relative relative = relativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relative not found with id: " + id));
        relativeRepository.deleteById(id);
    }    
}

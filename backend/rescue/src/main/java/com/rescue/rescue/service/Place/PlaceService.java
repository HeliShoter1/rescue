package com.rescue.rescue.service.Place;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.PlaceRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.request.CreatePlace;
import com.rescue.rescue.sercurity.user.RescueUserDetail;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@Transactional
@AllArgsConstructor
public class PlaceService implements IPlaceService {
    private final PlaceRepository placeRepository;

    private final UserReponsitory userRepository;

    @Override
    public PlaceDto getPlaceById(Long id) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place not found"));
        return PlaceDto.fromEntity(place);
    }

    @Override
    public PlaceDto createPlace(CreatePlace placeDto) {
        Place place = Place.builder()
                .name(placeDto.getName())
                .latitude(placeDto.getLatitude())
                .longtude(placeDto.getLongtude())
                .build();
        placeRepository.save(place);
        return PlaceDto.fromEntity(place);
    }

    @Override
    public void deletePlaceById(Long id) {
        Place place = placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place not found"));
         Authentication authentication ;
        Long userId;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getPlace() == null || !user.getPlace().getId().equals(id) ) {
            throw new SecurityException("You do not have permission to delete this place");
        }
        userRepository.clearPlaceById(userId);
        placeRepository.delete(place);
    }
    
}
package com.rescue.rescue.service.Place;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.enums.TypePlace;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.PlaceRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
import com.rescue.rescue.request.CreatePlace;
import com.rescue.rescue.sercurity.user.RescueUserDetail;
import com.rescue.rescue.service.Notification.INotificationService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@Transactional
@AllArgsConstructor
public class PlaceService implements IPlaceService {
    private final PlaceRepository placeRepository;

    private final UserReponsitory userRepository;

    private final INotificationService notificationService;

    private static final double EARTH_RADIUS_KM = 6371.0;

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
                .typePlace(placeDto.getTypePlace())
                .build();
        if(place.getTypePlace() == TypePlace.DANGEROUS_AREA){
                Authentication authentication ;
                Long userId;
                try {
                    authentication = SecurityContextHolder.getContext().getAuthentication();
                    userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
                } catch (Exception e) {
                    throw new ResourceNotFoundException("User not found");
                }
                List<User> users = userRepository.findAllUserFilterPlace().stream()
                                                .filter(user -> calculateDistance(
                                                        place.getLatitude(), place.getLongtude(),
                                                        user.getPlace().getLatitude(), user.getPlace().getLongtude()
                                                ) <= 1.0)
                                                .collect(Collectors.toList());
                notificationService.notifyAllUser(users, userId, "Dangerous area alert", "A dangerous area has been reported near your location. Please stay safe and avoid the area.");
                
        }
        placeRepository.save(place);
        return PlaceDto.fromEntity(place);
    }

    @Override
    public List<PlaceDto> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return places.stream().map(PlaceDto::fromEntity).toList();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
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
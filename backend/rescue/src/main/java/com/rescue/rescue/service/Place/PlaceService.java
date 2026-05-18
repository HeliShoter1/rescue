package com.rescue.rescue.service.Place;

import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.reponsitory.PlaceRepository;
import com.rescue.rescue.request.CreatePlace;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@Transactional
@AllArgsConstructor
public class PlaceService implements IPlaceService {
    private final PlaceRepository placeRepository;

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
                .longitude(placeDto.getLongtude())
                .build();
        placeRepository.save(place);
        return PlaceDto.fromEntity(place);
    }

    @Override
    public void deletePlaceById(Long id) {
        // TODO Auto-generated method stub
        Place place = placeRepository.findById(id).orElseThrow(() -> new RuntimeException("Place not found"));
        placeRepository.delete(place);
    }
    
}
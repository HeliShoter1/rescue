package com.rescue.rescue.service.Place;

import java.util.List;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.request.CreatePlace;

public interface IPlaceService {
    PlaceDto getPlaceById(Long id);
    PlaceDto getPlaceOfUserById();
    PlaceDto createPlace(CreatePlace placeDto);
    List<PlaceDto> getAllPlaces();
    void deletePlaceById(Long id);
}

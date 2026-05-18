package com.rescue.rescue.service.Place;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.request.CreatePlace;

public interface IPlaceService {
    PlaceDto getPlaceById(Long id);
    PlaceDto createPlace(CreatePlace placeDto);
    void deletePlaceById(Long id);
}

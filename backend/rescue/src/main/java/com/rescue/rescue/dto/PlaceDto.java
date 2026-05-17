package com.rescue.rescue.dto;

import com.rescue.rescue.model.Place;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class PlaceDto {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private UserDto user;

    public static PlaceDto fromEntity(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .user(UserDto.fromEntity(place.getUser()))
                .build();
        }
}

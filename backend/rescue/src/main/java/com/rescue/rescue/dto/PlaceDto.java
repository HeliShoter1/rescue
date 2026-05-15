package com.rescue.rescue.dto;

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
}

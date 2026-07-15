package com.rescue.rescue.service.Weather;

import org.springframework.web.client.RestTemplate;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.dto.WeatherDto;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.service.Place.IPlaceService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WeatherService {
    
    @Value("${openweather.api-key}")
    private String apiKey;

    private final IPlaceService placeService;
    private final RestTemplate restTemplate;

    public WeatherDto getCurrentWeather() {

        PlaceDto place = placeService.getPlaceOfUserById();

        String url = String.format(
            "https://api.openweathermap.org/data/2.5/weather"
            + "?lat=%f"
            + "&lon=%f"
            + "&appid=%s"
            + "&units=metric"
            + "&lang=vi",
            place.getLatitude(),
            place.getLongitude(),
            apiKey
        );

        return restTemplate.getForObject(url, WeatherDto.class);
    }

}

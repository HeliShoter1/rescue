package com.rescue.rescue.service.Weather;

import org.springframework.web.client.RestTemplate;

import com.rescue.rescue.dto.PlaceDto;
import com.rescue.rescue.dto.WeatherDto;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.service.Place.IPlaceService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor   
@Service
public class WeatherService {
    
    @Value("${openweather.api-key}")
    private String apiKey;

    private final IPlaceService placeService;
    private final RestTemplate restTemplate;

    public WeatherDto.Current getCurrentWeather() {

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

        return restTemplate.getForObject(url, WeatherDto.Current.class);
    }

    public WeatherDto.Forecast getForecastWeather(int days) {

    PlaceDto place = placeService.getPlaceOfUserById();

    String url = String.format(
        "https://api.openweathermap.org/data/2.5/forecast"
            + "?lat=%f"
            + "&lon=%f"
            + "&appid=%s"
            + "&units=metric"
            + "&lang=vi",
        place.getLatitude(),
        place.getLongitude(),
        apiKey
    );

    WeatherDto.Forecast dto = restTemplate.getForObject(url, WeatherDto.Forecast.class);

    if (dto == null || dto.getList() == null) {
        return dto;
    }

    Set<String> targetTimes = Set.of(
        "06:00:00",
        "12:00:00",
        "18:00:00"
    );

    List<WeatherDto.ForecastItem> filtered = dto.getList().stream()
            .filter(item -> {
                String time = item.getDtTxt().substring(11);
                return targetTimes.contains(time);
            })
            .toList();

    dto.setList(filtered);

    return dto;
}

}

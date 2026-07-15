package com.rescue.rescue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.service.Weather.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/weather")
public class weatherController {
    
    private final WeatherService weatherService;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse> getCurrentWeather() {
        return ResponseEntity.ok(
            new ApiResponse(
                "Success",
                weatherService.getCurrentWeather()
            )
        );
    }
}

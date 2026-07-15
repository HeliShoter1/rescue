package com.rescue.rescue.dto;

import java.util.List;

import lombok.Data;

@Data
public class WeatherDto {
    private String name;
    private Main main;
    private Wind wind;
    private List<Weather> weather;

    @Data
    public static class Main {
        private Double temp;
        private Double feels_like;
        private Integer humidity;
    }

    @Data
    public static class Wind {
        private Double speed;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }
}

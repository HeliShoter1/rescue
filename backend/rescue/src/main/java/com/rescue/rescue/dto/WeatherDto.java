package com.rescue.rescue.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

public class WeatherDto {

    @Data
    public static class Current {
        private String name;
        private Main main;
        private Wind wind;
        private List<Weather> weather;
    }

    @Data
    public static class Forecast {
        private City city;
        private List<ForecastItem> list;
    }

    @Data
    public static class ForecastItem {

        private Long dt;

        @JsonProperty("dt_txt")
        private String dtTxt;

        private Main main;
        private Wind wind;
        private Double pop;
        private List<Weather> weather;
    }

    @Data
    public static class City {
        private String name;
        private String country;
    }

    @Data
    public static class Main {

        private Double temp;

        @JsonProperty("feels_like")
        private Double feelsLike;

        @JsonProperty("temp_min")
        private Double tempMin;

        @JsonProperty("temp_max")
        private Double tempMax;

        private Integer pressure;
        private Integer humidity;
    }

    @Data
    public static class Wind {
        private Double speed;
        private Integer deg;
        private Double gust;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
        private String icon;
    }

}
package com.weatherforecast.city.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherDTO {
    private Double temp;
    private int pressure;
    private float humidity;
    private String dateTime;
    private String city;
    private float averageDayTemperature;
    private float averageNightTemperature;
    private float averagePressure;



}

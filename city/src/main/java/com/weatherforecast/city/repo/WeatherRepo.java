package com.weatherforecast.city.repo;

import com.weatherforecast.city.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepo extends JpaRepository<Weather,Long> {


    Optional<Weather> findBycityName(String cityName);
}

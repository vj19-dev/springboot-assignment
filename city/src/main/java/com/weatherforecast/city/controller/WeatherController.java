package com.weatherforecast.city.controller;

import com.weatherforecast.city.DTO.WeatherDTO;
import com.weatherforecast.city.exception.Messages;
import com.weatherforecast.city.model.Weather;
import com.weatherforecast.city.repo.WeatherRepo;
import com.weatherforecast.city.service.WeatherService;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
@EnableCaching
public class WeatherController {

    @Autowired
    private WeatherService weatherService;


    @GetMapping("/data")
    public List<Weather> fetchCityAverage(){
        return weatherService.fetchCitys();
    }

    @GetMapping("/data/cityName/{cityName}")
    public ResponseEntity<?> findByCityName(@PathVariable String cityName){

//        if(weatherDTO != null){
//            return  ResponseEntity.ok(weatherDTO);
//        }
//        else{
//           Messages m = new Messages("City Not Found:"+cityName);
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
//        }
//        return new ResponseEntity<>(weatherService.fetchCityByName(cityName),"City not found");
        WeatherDTO weatherData = weatherService.fetchCityByName(cityName);

        if (weatherData == null) {
             return new ResponseEntity<>("City not found", HttpStatus.NOT_FOUND);
        } else {
          return new ResponseEntity<>(weatherData, HttpStatus.OK);
        }
    }

}

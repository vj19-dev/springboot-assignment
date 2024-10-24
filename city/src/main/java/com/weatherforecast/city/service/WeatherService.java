package com.weatherforecast.city.service;

import com.weatherforecast.city.exception.Messages;
import com.weatherforecast.city.model.Weather;
import com.weatherforecast.city.repo.WeatherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import  com.weatherforecast.city.DTO.*;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherRepo weatherRepo;



    private String apiKey = "ad130312bb4978d03e416f8159ca3c96";
   // private String apiCityWiseURL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
   // private String apiGroupURL = "http://api.openweathermap.org/data/2.5/group?id=%s&appid=%s";
    private String apiCityWiseURL = "http://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric";
    ArrayList<String> cityNames = new ArrayList<>();

    public void storeCity() {
        cityNames.add("Paris");
        cityNames.add("Tokyo");
        cityNames.add("Bordeaux");
        cityNames.add("Lyon");
        cityNames.add("Lille");
        cityNames.add("Strasbourg");
        cityNames.add("Marseille");
        cityNames.add("Dijon");
        cityNames.add("Nantes");
        cityNames.add("Ahmedabad");
        List<WeatherDTO> weatherDetailsList = new ArrayList<>();



        try {
            for (String city : cityNames) {
                String URL = String.format(apiCityWiseURL, city, apiKey);
                ResponseEntity<Map> response =   restTemplate.getForEntity(URL, Map.class);

               Map<String,Object> body = response.getBody();
                if(body!=null){

                   Object listObject = body.get("list");
                  // Map<String,Object> citydata = (Map<String, Object>) body.get("city");

                   System.out.println("Object:"+listObject);
                    List<Map<String,Object>> listweatehrData = (List<Map<String,Object>>)listObject;

                    for (Map<String,Object> data:listweatehrData){

                        WeatherDTO weatherDTO = new WeatherDTO();
                        Map<String,Object> innerData = (Map<String,Object>)data.get("main");

                        Number num_temp = (Number) innerData.get("temp");
                        Double temp = num_temp.doubleValue();
                        weatherDTO.setTemp(temp);
                        weatherDTO.setPressure((int) innerData.get("pressure"));
                        weatherDTO.setHumidity((int) innerData.get("humidity"));
                        weatherDTO.setDateTime((String) data.get("dt_txt"));
                        weatherDetailsList.add(weatherDTO);
                    }


                }
                calculateAndStoreAverages(weatherDetailsList,city);
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void calculateAndStoreAverages(List<WeatherDTO> weatherDetailsList, String city) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        float dayTempSum = 0;
        float nightTempSum = 0;
        int pressureSum = 0;
        int dayCount = 0;
        int nightCount = 0;

        for (WeatherDTO details : weatherDetailsList) {
            LocalDateTime dateTime = LocalDateTime.parse(details.getDateTime(), formatter);
            int hour = dateTime.getHour();


            if (hour >= 6 && hour < 18) {
                dayTempSum += details.getTemp();
                pressureSum += details.getPressure();
                dayCount++;
            }

            else {
                nightTempSum += details.getTemp();
                pressureSum += details.getPressure();
                nightCount++;
            }
        }

        float averageDayTemp = dayCount > 0 ? dayTempSum / dayCount : 0;
        float averageNightTemp = nightCount > 0 ? nightTempSum / nightCount : 0;
        float averagePressure = (dayCount + nightCount) > 0 ? (float) pressureSum / (dayCount + nightCount) : 0;


        Weather weather = new Weather();
        weather.setCityName(city);
        weather.setPressure(averagePressure);
        weather.setTemperatureDay(averageDayTemp);
        weather.setTemperatureNight(averageNightTemp);

        weatherRepo.save(weather);


    }
    public List<Weather> fetchCitys(){

        return weatherRepo.findAll();
    }

    public WeatherDTO fetchCityByName(String cityName){
        Weather weatherOptional= weatherRepo.findBycityName(cityName).orElseThrow(() -> new Messages("City Not found"));
//        return  WeatherDTO.builder().city(weatherOptional.getCityName()).temp((double) weatherOptional.getTemperatureDay()).pressure((int) weatherOptional.getPressure()).build();
        return  WeatherDTO.builder().city(weatherOptional.getCityName()).temp((double) weatherOptional.getTemperatureDay()).pressure((int) weatherOptional.getPressure()).build();

    }

//    private WeatherDTO convertTODTO(Weather weather){
//        WeatherDTO weatherDTO =new WeatherDTO();
//        weatherDTO.setCity(weather.getCityName());
//        weatherDTO.setTemp((double) weather.getTemperatureDay());
//        weatherDTO.setHumidity(weather.getPressure());
//        return weatherDTO;
//    }
}
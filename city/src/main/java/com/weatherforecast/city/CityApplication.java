package com.weatherforecast.city;

import com.weatherforecast.city.DTO.WeatherDTO;
import com.weatherforecast.city.configuration.AppConfig;
import com.weatherforecast.city.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication

public class CityApplication implements CommandLineRunner {


	@Autowired
	private  WeatherService weatherService;
	public static void main(String[] args) {
		SpringApplication.run(CityApplication.class, args);
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		context.getBean(AppConfig.class);


	}
	@Override
	public void run(String... args) throws Exception {
		weatherService.storeCity();
	}

}

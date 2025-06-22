package ru.naumen.calorietracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CalorieTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalorieTrackerApplication.class, args);
	}

}

package com.assignment.videorental;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.assignment.videorental.infrastructure.config.Profiles;
import com.assignment.videorental.infrastructure.database.DataContainer;


@SpringBootApplication
public class VideorentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideorentalApplication.class, args);
	}

	@Bean
    @Profile(Profiles.TEST)
    public CommandLineRunner loadData(DataContainer dataContainer) {
		System.out.println("I am here ..");
        return (args) -> {
            dataContainer.initializeCustomers();
            dataContainer.initializeFilms();
        };
    }
}

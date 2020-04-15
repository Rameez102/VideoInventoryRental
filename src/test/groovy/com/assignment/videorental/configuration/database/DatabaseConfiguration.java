package com.assignment.videorental.configuration.database;

import com.assignment.videorental.configuration.customer.InMemoryCustomerRepository;
import com.assignment.videorental.configuration.film.InMemoryFilmRepository;
import com.assignment.videorental.configuration.rental.InMemoryRentalOrderRepository;
import com.assignment.videorental.configuration.rental.InMemoryRentalRepository;
import com.assignment.videorental.customer.CustomerRepository;
import com.assignment.videorental.film.FilmRepository;
import com.assignment.videorental.infrastructure.database.DataContainer;
import com.assignment.videorental.rental.RentalOrderRepository;
import com.assignment.videorental.rental.RentalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {
    @Bean
    public DataContainer dataContainer(FilmRepository filmRepository, CustomerRepository customerRepository,
                                       RentalRepository rentalRepository, RentalOrderRepository rentalOrderRepository) {
        return new TestDataContainer(filmRepository, customerRepository, rentalRepository, rentalOrderRepository);
    }

    @Bean
    public RentalRepository rentalRepository() {
        return new InMemoryRentalRepository();
    }

    @Bean
    public RentalOrderRepository rentalOrderRepository() {
        return new InMemoryRentalOrderRepository();
    }

    @Bean
    public FilmRepository filmRepository() {
        return new InMemoryFilmRepository();
    }

    @Bean
    public CustomerRepository customerRepository() {
        return new InMemoryCustomerRepository();
    }
}

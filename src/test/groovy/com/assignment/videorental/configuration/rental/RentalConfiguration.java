package com.assignment.videorental.configuration.rental;

import com.assignment.videorental.configuration.TimeConfiguration;
import com.assignment.videorental.configuration.customer.CustomerConfiguration;
import com.assignment.videorental.configuration.database.DatabaseConfiguration;
import com.assignment.videorental.configuration.film.FilmConfiguration;
import com.assignment.videorental.customer.CustomerRepository;
import com.assignment.videorental.film.FilmFacade;
import com.assignment.videorental.film.FilmRepository;
import com.assignment.videorental.rental.*;
import com.assignment.videorental.shared.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FilmConfiguration.class, CustomerConfiguration.class, TimeConfiguration.class, DatabaseConfiguration.class})
public class RentalConfiguration {

    @Bean
    public RentalFacade rentalFacade(RentalRepository rentalRepository, FilmRepository filmRepository,
                                     CustomerRepository customerRepository, RentalOrderRepository rentalOrderRepository,
                                     RentalFactory rentalFactory, TimeProvider timeProvider, RentalMapper rentalOrderMapper) {
        return new RentalFacade(customerRepository, filmRepository, rentalRepository,
                rentalOrderRepository, rentalFactory, rentalOrderMapper, timeProvider);
    }

    @Bean
    public RentalBoxStorage rentalBoxStorage() {
        return new RentalBoxStorage();
    }

    @Bean
    public RentalFactory rentalFactory() {
        return new RentalFactory();
    }

    @Bean
    public RentalMapper rentalOrderMapper() {
        return new RentalMapper();
    }

    @Bean
    public RentalController rentalController(RentalFacade rentalFacade, RentalBoxStorage rentDraftStorage, FilmFacade filmFacade) {
        return new RentalController(rentalFacade, filmFacade, rentDraftStorage);
    }

}

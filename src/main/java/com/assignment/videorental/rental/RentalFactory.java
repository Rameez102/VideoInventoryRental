package com.assignment.videorental.rental;

import com.assignment.videorental.customer.Customer;
import com.assignment.videorental.film.FilmType;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class RentalFactory {

    public Rental create(Customer customer, LocalDate rentDate, FilmType filmType, LocalDate expectedReturnDate) {
        return Rental.builder()
                .filmType(filmType)
                .rentalStatus(RentalStatus.STARTED)
                .customer(customer)
                .price(BigDecimal.ZERO)
                .surcharge(BigDecimal.ZERO)
                .rentDate(rentDate)
                .expectedReturnDate(expectedReturnDate)
                .build();
    }

    public RentalOrder create(List<Rental> rentals) {
        return new RentalOrder(rentals);
    }
}

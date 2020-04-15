package com.assignment.videorental.rental;

import org.springframework.stereotype.Component;

import com.assignment.videorental.film.FilmType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RentalMapper {

    public RentalDTO toDTO(Rental rental) {
        Optional<FilmType> maybeFilmType = Optional.ofNullable(rental.getFilmType());
        Optional<RentalStatus> maybeStatus = Optional.ofNullable(rental.getRentalStatus());
        return RentalDTO.builder()
                .id(rental.getId())
                .customerId(rental.getCustomer().getId())
                .actualReturnDate(rental.getActualReturnDate())
                .expectedReturnDate(rental.getExpectedReturnDate())
                .rentDate(rental.getRentDate())
                .filmType(maybeFilmType.map(Enum::name).orElse(null))
                .price(Optional.ofNullable(rental.getPrice()).orElse(BigDecimal.ZERO))
                .surcharge(Optional.ofNullable(rental.getSurcharge()).orElse(BigDecimal.ZERO))
                .status(maybeStatus.map(Enum::name).orElse(null))
                .build();
    }

    public RentalOrderDTO toDTO(RentalOrder rentalOrder) {
        return RentalOrderDTO.builder()
                .id(rentalOrder.getId())
                .rentals(rentalOrder.getRentals()
                        .stream()
                        .map(this::toDTO).collect(Collectors.toList()))
                .totalPrice(rentalOrder.getTotalPrice())
                .totalSurcharge(rentalOrder.getTotalSurcharge())
                .build();

    }
}

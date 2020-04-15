package com.assignment.videorental.rental;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assignment.videorental.customer.Customer;
import com.assignment.videorental.customer.CustomerNotFoundException;
import com.assignment.videorental.customer.CustomerRepository;
import com.assignment.videorental.film.Film;
import com.assignment.videorental.film.FilmNotFoundException;
import com.assignment.videorental.film.FilmRepository;
import com.assignment.videorental.shared.time.TimeProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RentalFacade {

    private CustomerRepository customerRepository;
    private FilmRepository filmRepository;
    private RentalRepository rentalRepository;
    private RentalOrderRepository rentalOrderRepository;
    private RentalFactory rentalFactory;
    private RentalMapper rentalMapper;
    private TimeProvider timeProvider;

    public RentalOrderDTO completeOrder(Long customerId, RentalOrderDraftDTO rentalOrderDraft) {
        List<Rental> rentals = rentalOrderDraft.getFilms()
                .stream()
                .map(rentFilmEntry -> rent(customerId, rentFilmEntry.getFilmId(), rentFilmEntry.getNumberOfDays()))
                .collect(Collectors.toList());

        return Optional.ofNullable(rentalFactory.create(rentals))
                .map(rental -> rentalOrderRepository.save(rental))
                .map(rentalMapper::toDTO)
                .orElseThrow(() -> new CannotCreateRentalOrderException(Collections
                        .singletonMap("customerId", String.valueOf(customerId))));
    }

    public RentalDTO returnFilm(Long id) {
        return rentalRepository.findById(id)
                .map(rental -> rental.returnFilm(timeProvider.today()))
                .map(rental -> rentalMapper.toDTO(rental))
                .orElseThrow(() -> new RentalNotFoundException(Collections
                        .singletonMap("id", String.valueOf(id))));
    }

    public RentalOrderDTO find(Long rentalOrderId) {
        return rentalOrderRepository.findById(rentalOrderId)
                .map(rental -> rentalMapper.toDTO(rental))
                .orElseThrow(() -> new RentalOrderNotFoundException(Collections
                        .singletonMap("id", String.valueOf(rentalOrderId))));
    }

    private Rental rent(Long customerId, Long filmId, Integer numberOfDays) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(Collections
                        .singletonMap("id", String.valueOf(customerId))));

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(Collections.singletonMap("id", String.valueOf(filmId))));

        customer.increaseBonusPoints(film.getType().getBonusPoints());

        LocalDate today = timeProvider.today();

        return Optional.ofNullable(rentalFactory.create(customer, today, film.getType(), today.plusDays(numberOfDays)))
                .map(rental -> rental.rent(timeProvider.today()))
                .map(rentalRepository::save)
                .orElseThrow(() -> new CannotCreateRentalOrderException(Collections.singletonMap("filmId", String.valueOf(filmId))));
    }
}

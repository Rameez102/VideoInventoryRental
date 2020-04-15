package com.assignment.videorental.customer;

import com.assignment.videorental.shared.CurrencyType;
import com.assignment.videorental.shared.domain.PersonalData;

import org.springframework.stereotype.Component;

@Component
public class CustomerFactory {

    private static final Integer DEFAULT_BONUS_POINTS = 0;

    public Customer create(PersonalData personalData) {
        return Customer.builder()
                .bonusPoints(DEFAULT_BONUS_POINTS)
                .currency(CurrencyType.EUR)
                .personalData(personalData)
                .build();
    }
}

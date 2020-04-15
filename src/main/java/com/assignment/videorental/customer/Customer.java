package com.assignment.videorental.customer;

import com.assignment.videorental.shared.CurrencyType;
import com.assignment.videorental.shared.domain.PersonalData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private Integer bonusPoints = 0;

    @Embedded
    private PersonalData personalData;

    private CurrencyType currency = CurrencyType.EUR;

    public void increaseBonusPoints(Integer bonusPoints) {
        this.bonusPoints += bonusPoints;
    }

    public String getEmail() {
        return this.personalData.getEmail();
    }

}

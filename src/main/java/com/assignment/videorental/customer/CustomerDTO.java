package com.assignment.videorental.customer;

import com.assignment.videorental.shared.dto.PersonalDataDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private Integer bonusPoints;
    private PersonalDataDTO personalData;
    private String currency;
}

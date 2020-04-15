package com.assignment.videorental.customer;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.assignment.videorental.shared.dto.PersonalDataDTO;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController implements CustomerSwaggerDocumentation {

    private final CustomerFacade customerFacade;

    @GetMapping(value = "/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId) {
        return customerFacade.getCustomer(customerId);
    }

    @PostMapping
    public CustomerDTO create(@RequestBody PersonalDataDTO personalData) {
        return customerFacade.createCustomer(personalData);
    }
}

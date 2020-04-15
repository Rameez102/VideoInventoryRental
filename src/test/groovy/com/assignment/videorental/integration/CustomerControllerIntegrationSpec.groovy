package com.assignment.videorental.integration


import com.assignment.videorental.configuration.customer.CustomerConfiguration
import com.assignment.videorental.configuration.database.DatabaseConfiguration
import com.assignment.videorental.customer.CustomerController
import com.assignment.videorental.customer.CustomerDTO
import com.assignment.videorental.infrastructure.IntegrationSpec
import com.assignment.videorental.infrastructure.config.Profiles
import com.assignment.videorental.shared.CurrencyType
import com.assignment.videorental.shared.dto.PersonalDataDTO
import com.assignment.videorental.shared.time.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Profile(Profiles.TEST)
@ContextConfiguration(classes = [
        CustomerConfiguration.class,
        TimeProvider.class,
        DatabaseConfiguration.class
])
@SpringBootTest
class CustomerControllerIntegrationSpec extends IntegrationSpec {

    @Autowired
    private CustomerController customerController

    def setup() {

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(restControllerAdvice)
                .alwaysDo(MockMvcResultHandlers.print())
                .build()
        dataContainer.initializeCustomers()
    }


    def cleanup() {
        dataContainer.cleanUp()
    }

    def 'should get details about customer'() {
        when: 'I ask about customer details'
        CustomerDTO customer = dataContainer.customerDTO()
        Long customerId = customer.id

        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customers/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        then: 'I get customer details'
        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))
    }

    def 'should create new customer'() {
        when: 'I create new customer'
        PersonalDataDTO personalData = PersonalDataDTO.builder()
                .email("john@google.com")
                .firstName("John")
                .lastName("Smith")
                .build()

        ResultActions customerResponse = this.mockMvc
                .perform(post('/api/customers')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then: 'New customer has been created'
        Long customerId = 2L
        CustomerDTO customer = CustomerDTO.builder()
                .id(customerId)
                .bonusPoints(0)
                .personalData(personalData)
                .currency(CurrencyType.EUR.name())
                .build()

        customerResponse
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        and: 'Customer is stored in database'
        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customers/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

    }

    def 'should not create same customer twice'() {
        when: 'I create new customer'
        PersonalDataDTO personalData = PersonalDataDTO.builder()
                .email("john@google.com")
                .firstName("John")
                .lastName("Smith")
                .build()

        ResultActions createCustomerResultAction = this.mockMvc
                .perform(post('/api/customers')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then: 'New customer has been created'
        Long customerId = 2L
        CustomerDTO customer = CustomerDTO.builder()
                .id(customerId)
                .bonusPoints(0)
                .personalData(personalData)
                .currency(CurrencyType.EUR.name())
                .build()

        createCustomerResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        and: 'Customer is stored in database'
        ResultActions getCustomerDetailsResultAction = this.mockMvc
                .perform(get('/api/customers/{customerId}', customerId)
                .contentType(MediaType.APPLICATION_JSON))

        getCustomerDetailsResultAction
                .andExpect(status().isOk())
                .andExpect(content().json(buildJson(customer)))

        when: 'I create customer with same data twice'
        ResultActions createCustomerFailureResultAction = this.mockMvc
                .perform(post('/api/customers')
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildJson(personalData)))

        then: 'I cannot create such customer'
        createCustomerFailureResultAction
                .andExpect(status()
                .is4xxClientError())
    }
}

package com.amigoscode.notifications.customer.service;

import com.amigoscode.notifications.clients.fraud.FraudCheckResponse;
import com.amigoscode.notifications.clients.fraud.FraudClient;
import com.amigoscode.notifications.customer.model.Customer;
import com.amigoscode.notifications.customer.model.CustomerRegistrationRequest;
import com.amigoscode.notifications.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstname())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);
        // todo: check if fraudster
        FraudCheckResponse fraudsterResponse = fraudClient.isFraudster(customer.getId());
        if (fraudsterResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        // todo: send notification

    }
}

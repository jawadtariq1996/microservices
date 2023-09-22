package com.amigoscode.customer.service;

import com.amigoscode.clients.FraudCheckResponse;
import com.amigoscode.clients.FraudClient;
import com.amigoscode.clients.NotificationClient;
import com.amigoscode.clients.NotificationRequest;
import com.amigoscode.customer.model.Customer;
import com.amigoscode.customer.model.CustomerRegistrationRequest;
import com.amigoscode.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        // todo: check if email valid
        // todo: check if email not taken
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudsterResponse = fraudClient.isFraudster(customer.getId());
        if (fraudsterResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                "Hi %s, welcome to amigoscode" + customer.getFirstName()
        );

        notificationClient.sendNotification(notificationRequest);
    }
}

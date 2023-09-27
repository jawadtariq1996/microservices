package com.amigoscode.customer.controller;

import com.amigoscode.customer.model.Customer;
import com.amigoscode.customer.model.CustomerRegistrationRequest;
import com.amigoscode.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping()
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
    }

    @GetMapping
    public List<Customer> getCustomersWithCriteria(
            @QueryParam("firstName") String firstName
    ) {
        return customerService.getCustomerWithCriteria(firstName);
    }

    @GetMapping("filtered")
    public ResponseEntity<Page<Customer>> getFilteredCustomers(
            @QueryParam("firstName") String firstName,
            @QueryParam("lastName") String lastName,
            @QueryParam("email") String email,
            @RequestParam("searchQuery") String searchQuery,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Page<Customer> filteredCustomers = customerService.getFilteredCustomers(firstName, lastName, email, searchQuery, page, size);
        return new ResponseEntity<>(filteredCustomers, HttpStatus.OK);
    }
}

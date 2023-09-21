package com.amigoscode.customer.model;

public record CustomerRegistrationRequest(String firstname,
                                          String lastName,
                                          String email) {
}

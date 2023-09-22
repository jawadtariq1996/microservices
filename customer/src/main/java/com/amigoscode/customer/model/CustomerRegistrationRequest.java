package com.amigoscode.notifications.customer.model;

public record CustomerRegistrationRequest(String firstname,
                                          String lastName,
                                          String email) {
}

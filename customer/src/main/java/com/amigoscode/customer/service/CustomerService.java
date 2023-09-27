package com.amigoscode.customer.service;

import com.amigoscode.clients.FraudCheckResponse;
import com.amigoscode.clients.FraudClient;
import com.amigoscode.clients.NotificationClient;
import com.amigoscode.clients.NotificationRequest;
import com.amigoscode.customer.model.Customer;
import com.amigoscode.customer.model.CustomerRegistrationRequest;
import com.amigoscode.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;
    private final EntityManager entityManager;

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

    public List<Customer> getCustomerWithCriteria(String firstName) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder(); // 1- create builder

        CriteriaQuery<Customer> query = builder.createQuery(Customer.class); // 2- create criteria query

        Root<Customer> customerRoot = query.from(Customer.class); // 3- create root of target entity


        CriteriaQuery<Customer> select = query.select(customerRoot); // 4-i - make any query with query instance

        TypedQuery<Customer> query1 = entityManager.createQuery(select); // 5-i- pass it to entity manager to get typed query


        CriteriaQuery<Customer> queryForGetCustomersWithFirstName = query.where(
                builder.equal(customerRoot.get("firstName"),
                        firstName
                )); // 4-ii - make any query with query instance


        TypedQuery<Customer> queryCustomersWithFirstName = entityManager.createQuery(queryForGetCustomersWithFirstName); // 5-ii- pass it to entity manager
        return queryCustomersWithFirstName.getResultList(); // 6 get results from query
    }

    public Page<Customer> getFilteredCustomers(
            String firsName,
            String lastName,
            String email,
            String searchQuery,
            int page,
            int size
    ) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = query.from(Customer.class);

        Pageable pageable = PageRequest.of(page, size);
        List<String> customerFields = getCustomerFields();

        List<Predicate> predicatesOr = getSearchPredicates(
                customerFields,
                searchQuery,
                criteriaBuilder,
                customerRoot
        );

        List<Predicate> predicatesAnd = getAdvancePredicates(
                firsName,
                lastName,
                email,
                criteriaBuilder,
                customerRoot
        );

        query.where(
                criteriaBuilder.and(predicatesAnd.toArray(new Predicate[0])),
                criteriaBuilder.or(predicatesOr.toArray(new Predicate[0]))
        );
        

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Customer> customerRootCount = countQuery.from(Customer.class);

        countQuery
                .select(criteriaBuilder.count(customerRootCount));

        Long count = entityManager.createQuery(countQuery).getSingleResult();

        List<Customer> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList, pageable, count);
    }


    private List<Predicate> getAdvancePredicates(
            String firsName,
            String lastName,
            String email,
            CriteriaBuilder criteriaBuilder,
            Root<Customer> customerRoot) {

        List<Predicate> predicates = new ArrayList<>();


        if (!firsName.isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(
                            customerRoot.get("firstName"),
                            firsName)
            );
        }

        if (!lastName.isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(
                            customerRoot.get("lastName"),
                            lastName)
            );
        }

        if (!email.isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(
                            customerRoot.get("email"),
                            email)
            );
        }

        return predicates;
    }


    public List<Predicate> getSearchPredicates(List<String> fieldNames, String searchQuery, CriteriaBuilder criteriaBuilder, Root<Customer> customerRoot) {
        return fieldNames.stream()
                .filter(Objects::nonNull)
                .map(fieldName -> criteriaBuilder.like(
                        customerRoot.get(fieldName).as(String.class),
                        "%" + searchQuery + "%"))
                .collect(Collectors.toList());
    }

    private List<String> getCustomerFields() {
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("firstName");
        fieldNames.add("lastName");
        fieldNames.add("email");
        return fieldNames;
    }
}





















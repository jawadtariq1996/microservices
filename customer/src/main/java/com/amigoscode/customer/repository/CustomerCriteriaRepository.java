//package com.amigoscode.customer.repository;
//
//import com.amigoscode.customer.model.Customer;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.TypedQuery;
//import java.util.List;
//
//@Repository
//public class CustomerCriteriaRepository {
//
//    private final TypedQuery<Customer> typedQuery;
//
//    public CustomerCriteriaRepository(TypedQuery<Customer> typedQuery) {
//        this.typedQuery = typedQuery;
//    }
//
//    public void getCustomerWithCriteria() {
//        List<Customer> resultList = typedQuery.getResultList();
//        System.out.println(resultList);
//    }
//}

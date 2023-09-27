package com.amigoscode.customer.config;

import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Configuration
public class CriteriaQueryConfig {

    private final EntityManager entityManager;

    public CriteriaQueryConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T> TypedQuery<T> getQueryObject(Class<T> t) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(t);
        Root<T> tRoot = query.from(t);

        CriteriaQuery<T> select = query.select(tRoot);
        return entityManager.createQuery(select);
    }

}

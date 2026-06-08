package com.SpringPractice.store.repositories;

import com.SpringPractice.store.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor//needs it because entity manager is final but not initialized in a constructor
@Repository //so spring can create instances at runtime
//implementation of the findProductsByCriteria function
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository {
    @PersistenceContext//Instructs the container to inject a managed EntityManager instance.
    private final EntityManager entityManager; //working directly with hibernate and db to create query dynamically

    @Override
    public List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder(); //get the criteria builder object
        CriteriaQuery<Product> cq = cb.createQuery(Product.class); //create the criteria query object
        //sql: from products (part of query at runtime)
        Root<Product> root = cq.from(Product.class); //root entity/table

        //dynamic conditions/filters
        List<Predicate> predicates = new ArrayList<>(); //conditions: if not null, add to predicate list
        if (name != null) {
            // name like %name%
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }
        if (minPrice != null) {
            // price >= minPrice
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            // price <= minPrice
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        //build the query: convert list of predicates to array
        cq.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        //construct the query and execute it
        return entityManager.createQuery(cq).getResultList();
    }
}

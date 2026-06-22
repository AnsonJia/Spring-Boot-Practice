package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    //optimization of querying so we only need to query the db once instead of multiple for products/category
    @EntityGraph(attributePaths = "items.product")//when loading cart, fetch the cart items and the product of each item
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")//find carts with the id in param
    Optional<Cart> getCartWithItems(@Param("cartId") UUID id);
}
package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CheckoutRequest;
import com.codewithmosh.store.dtos.CheckoutResponse;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderItem;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;


    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request) {
        //cartRepository.findById(request.getCartId());
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);//get the cart with all items in it
        if (cart == null) {//check if cart exists
            return ResponseEntity.badRequest().body(Map.of("error", "Cart not found"));
        }
        if (cart.getItems().isEmpty()) {//check if the cart contains items
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }

        //manually map cart to order instead of using MapStruct to entity(not a direct simple conversion easier to manually map)
        var order = new Order();//create a new order object
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());//get the current user as the customer

        cart.getItems().forEach(item -> {//iterate over cart items and convert them into an orderItem
           var orderItem = new OrderItem();//new object
           orderItem.setOrder(order);
           orderItem.setProduct(item.getProduct());
           orderItem.setQuantity(item.getQuantity());
           orderItem.setTotalPrice(item.getTotalPrice());
           orderItem.setUnitPrice(item.getProduct().getPrice());//unit price can be found in the product price
           order.getItems().add(orderItem);//add order items to the order
        });

        orderRepository.save(order);//save the order
        cartService.clearCart(cart.getId());//clear all items from the cart

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }

}

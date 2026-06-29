package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service //service annotation so spring can create instances of this at runtime
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart = new Cart(); //create cart object and save it
        cartRepository.save(cart);

        return cartMapper.toDto(cart); //map cart to dto
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        //var cart = cartRepository.findById(cartId).orElse(null); //check if cartId is valid/exists
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//updated with new custom query method (functionally same as findById)
        if (cart == null) {
            //return ResponseEntity.notFound().build(); //service is below controller layer so it shouldn't know anything about http responses
            throw new CartNotFoundException(); //instead throw exception for the controller to handle
        }

        var product = productRepository.findById(productId).orElse(null);//validate product id from request
        if (product == null) {
            //return ResponseEntity.badRequest().build();//if null return 400 bad request not 404 because client sent invalid data in request
            throw new ProductNotFoundException();
        }
        //figure out if we should increment quantity of this item or create a new item
        var cartItem = cart.addItem(product); //use the addItem method in Cart entity to handle adding items to cart

        //instead of saving to a cartItem repository, save to cart repository (item is dependent on carts)
        cartRepository.save(cart);//the cart item will also be saved because of the cascade type we set in the cart entity (cascade = CascadeType.MERGE)
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        //var cart = cartRepository.findById(cartId).orElse(null);//check if cart exists
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//we can replace findById with our new custom query method
        if (cart == null) {
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        var cart =  cartRepository.getCartWithItems(cartId).orElse(null); //check if cart exists
        if (cart == null) {
            throw new CartNotFoundException();
        }
        //to validate productId, we don't use productRepository because it is unnecessary because we need to find cart item later anyway
        var cartItem = cart.getItem(productId); //use the new getItem method in the cart entity to find the cart item by product id
        if (cartItem == null) {//if invalid product, cartItem will be null, and then we can return an error
            throw new ProductNotFoundException();
        }
        cartItem.setQuantity(quantity);//update quantity
        cartRepository.save(cart);//save the cart

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//check if cart exists
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.removeItem(productId);//use removeItem method in the cart entity to remove the item by product id
        cartRepository.save(cart);//save changes
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//check if cart exists
        if (cart == null) {
            throw new CartNotFoundException();
        }
        cart.clear();//use the clear method in the cart entity to clear all items from the cart
        cartRepository.save(cart);//save changes
    }


}

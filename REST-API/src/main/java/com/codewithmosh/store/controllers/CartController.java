package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController //controllers are only supposed to deal with http request and response so put application logic in the service layer
@RequestMapping("/carts") //we usually use plural names
public class CartController {
    //private final CartRepository cartRepository; //no longer needed because we moved application logic to service layer
    //private final CartMapper cartMapper;
    //private final ProductRepository productRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(//we still need response entity here because we need the created status
            UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCart();
        //the generation of url/location is still the controllers responsibility
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();//uri for the created cart (for location header)
        //return new ResponseEntity<>(cartDto, HttpStatus.CREATED); //we could also do it this way without uri location header
        return ResponseEntity.created(uri).body(cartDto); //URIs are useful for public APIs
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(//we still need response entity here because we need the created status
            @PathVariable UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request //request dto for adding items to cart (product id) with valid annotation
    ){
        var cartItemDto = cartService.addToCart(cartId, request.getProductId()); //call the service method to add item to cart

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {//edge cases are being handled by method, no different response status so no point in using response entity
        return cartService.getCart(cartId);//call the service method to get cart by id and map it to dto
        //return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    //public ResponseEntity<?> updateItem(//change <CartItemDto> to <?> so we can return different types of responses (for the errors)
    public CartItemDto updateItem( //edge cases are being handled by method, no different response status so no point in using response entity
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request //valid annotation to validate the request body using the request dto
    ){
        return cartService.updateItem(cartId, productId, request.getQuantity()); //call the service method to update item in cart
        //return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        cartService.removeItem(cartId, productId); //call the service method to remove item from cart
        return ResponseEntity.noContent().build(); //return 204 no content response
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {//delete all items in the cart, not the cart
        cartService.clearCart(cartId); //call the service method to clear the cart
        return ResponseEntity.noContent().build(); //return 204 no content response
    }


    //exception handling
    @ExceptionHandler(CartNotFoundException.class) //automatically called when a CartNotFoundException is thrown anywhere in the controller
    public ResponseEntity<Map<String, String>> handleCartNotFound() { //instead of just .notFound().build(), we use status to return a message
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));//return a meaningful message
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {//automatically called when a ProductNotFoundException is thrown anywhere in the controller
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart."));
    }

}
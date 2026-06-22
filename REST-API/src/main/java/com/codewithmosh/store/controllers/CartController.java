package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts") //we usually use plural names
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart(); //create cart object and save it
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart); //map cart to dto
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();//uri for the created cart (for location header)
        //return new ResponseEntity<>(cartDto, HttpStatus.CREATED); //we could also do it this way without uri location header
        return ResponseEntity.created(uri).body(cartDto); //URIs are useful for public APIs
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request //request dto for adding items to cart (product id)
    ){
        //var cart = cartRepository.findById(cartId).orElse(null); //check if cartId is valid/exists
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//updated with new custom query method (functionally same as findById)

        if (cart == null) {
            return ResponseEntity.notFound().build(); //if not found return 404 not found
        }
        var product = productRepository.findById(request.getProductId()).orElse(null);//validate product id from request
        if (product == null) {
            return ResponseEntity.badRequest().build();//if null return 400 bad request not 404 because client sent invalid data in request
        }
        //check if the product is already in the cart (use stream to perform operations on a collection)
        var cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
        if  (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1); //if item already in cart, increase quantity by 1
        }else {
            cartItem = new CartItem(); //if item not in cart, create new cart item and add to cart
            cartItem.setProduct(product); //set product
            cartItem.setQuantity(1); //set quantity
            cartItem.setCart(cart); //set cart/update cart side of relationship (associate the cart item with the cart)
            cart.getItems().add(cartItem); //add cart item to the collection in the cart object
        }
        //instead of saving to a cartItem repository, save to cart repository (item is dependent on carts)
        cartRepository.save(cart);//the cart item will also be saved because of the cascade type we set in the cart entity (cascade = CascadeType.MERGE)
        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        //var cart = cartRepository.findById(cartId).orElse(null);//check if cart exists
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//we can replace findById with our new custom query method
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        var cartDto = cartMapper.toDto(cart);
        return ResponseEntity.ok(cartDto);
    }

}
package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
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
            @Valid @RequestBody AddItemToCartRequest request //request dto for adding items to cart (product id) with valid annotation
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
        //figure out if we should increment quantity of this item or create a new item
        var cartItem = cart.addItem(product); //use the addItem method in Cart entity to handle adding items to cart

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

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(//change <CartItemDto> to <?> so we can return different types of responses (for the errors)
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request //valid annotation to validate the request body using the request dto
    ){
        var cart =  cartRepository.getCartWithItems(cartId).orElse(null); //check if cart exists
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(//instead of just .notFound().build(), we use status to return a message
                    Map.of("error", "Cart not found.")//return a meaningful message to difference the not found errors
            );
        }
        //to validate productId, we don't use productRepository because it is unnecessary because we need to find cart item later anyway
        var cartItem = cart.getItem(productId); //use the new getItem method in the cart entity to find the cart item by product id
        if (cartItem == null) {//if invalid product, cartItem will be null, and then we can return an error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(//instead of just .notFound().build(), we use status to return a message
                    Map.of("error", "Product was not found in the cart.")//return a meaningful message to difference the not found errors
            );
        }
        cartItem.setQuantity(request.getQuantity());//update quantity
        cartRepository.save(cart);//save the cart

        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);//check if cart exists
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart was not found")
            );
        }
        cart.removeItem(productId);//use removeItem method in the cart entity to remove the item by product id
        cartRepository.save(cart);//save changes
        return ResponseEntity.noContent().build();
    }

}
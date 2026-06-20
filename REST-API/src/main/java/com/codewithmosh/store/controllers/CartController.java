package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/carts") //we usually use plural names
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

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
}

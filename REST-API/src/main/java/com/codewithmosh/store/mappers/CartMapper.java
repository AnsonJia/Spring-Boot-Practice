package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    //map total price to get total price from cart item entity
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")// dto field maps to the method in cartItem
    CartItemDto toDto(CartItem cartItem);
}

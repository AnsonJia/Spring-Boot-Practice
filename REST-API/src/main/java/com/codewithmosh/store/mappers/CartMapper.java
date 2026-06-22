package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    //@Mapping(target = "items", source = "items")//map cartItems in cart entity to items in dto (don't need mapping if source and target fields are the same)
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")//map totalPrice in dto to getTotalPrice() method in cart entity
    CartDto toDto(Cart cart); //mapper impl only maps id, not items or total price

    //map total price to get total price from cart item entity
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")// dto field maps to the method in cartItem
    CartItemDto toDto(CartItem cartItem);
}

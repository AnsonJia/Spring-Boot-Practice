package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")//maps category id to categoryId in productDto
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);//map dto to a product entity object

    @Mapping(target = "id",  ignore = true) //tell mapstruct to ignore id field when updating an existing product (we don't need to update id)
    void update(ProductDto productDto, @MappingTarget Product product);
}

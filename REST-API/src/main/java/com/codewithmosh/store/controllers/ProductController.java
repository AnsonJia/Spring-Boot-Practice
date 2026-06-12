package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor// to generate a constructor with all the fields (so spring can inject the dependencies)
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    //we could use ResponseEntity here to (ResponseEntity<List<ProductDto>> getAllProducts() )
    //but not necessary since spring will return 200 OK if we return a body and returning an empty list is valid response
    public List<ProductDto> getAllProducts(
            // request param gets value from query parameters (/....?param=x) usually used for filter/sort/search
            @RequestParam(name = "categoryId", required = false) Byte categoryId //best practice to give it a name
    ) {
        List<Product> products;
        if  (categoryId != null) { //based on if we have a category or not, we need to call 2 different methods of the repository
            products = productRepository.findByCategoryId(categoryId); //find products by category id (filter products by category)
        }else{
            products = productRepository.findAllWithCategory();//find all products
        }
        return products.stream().map(productMapper::toDto).toList();//map to a dto and turn to list before returning
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(// ResponseEntity for more response control (status code, headers, response body)
            @PathVariable Long id//path var gets values from url path (/.../var) usually used to identify a specific resource
    ) {
        var product = productRepository.findById(id).orElse(null);//find product by id, if not found return null
        if (product == null) {
            return ResponseEntity.notFound().build();//if product null return 404 not found
        }
        return ResponseEntity.ok(productMapper.toDto(product));//if not null, return product (after mapping to a dto)
    }
}

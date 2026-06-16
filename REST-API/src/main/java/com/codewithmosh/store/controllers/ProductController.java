package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor// to generate a constructor with all the fields (so spring can inject the dependencies)
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    //get all products
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

    //get product by id
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

    //create a new product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto, //we are not using a separate dto because most cases fields needed are the same
            UriComponentsBuilder uriBuilder
    ){
        //because product entity doesn't have a categoryId field (dto does), we lose it, so we have to fetch category separately
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null); //find the category
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(productDto);//map dto to entity (but product entity doesn't have categoryId, so fetch separately)
        product.setCategory(category); //before saving product, we set the category (product entity has a category field not id)
        productRepository.save(product);//save the product to the database (generates an id for the product)
        productDto.setId(product.getId());//manually set the id of the product dto for return so we don't need to map to dto again

        //build the location of the new resource (the new product) using the generated id
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        //return ResponseEntity.created(location).body(productMapper.toDto(product));
        // we don't need to map product to a new dto because request body dto already has everything except id (set manually)
        return ResponseEntity.created(uri).body(productDto); //return 201 created with the location header and the body of the new product
    }

    //update a product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ){
        var product = productRepository.findById(id).orElse(null); //check if the product we want to update exists
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        //because product entity doesn't have a categoryId field (dto does), we lose it, so we have to fetch category separately
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);//check if category exists
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        productMapper.update(productDto, product);//map dto to the entity, but we didn't pass in an id in dto so productId becomes null
        product.setCategory(category);//before saving product, we set the category (product entity has a category field not id)
        productRepository.save(product);//save the updated product to the database

        productDto.setId(product.getId()); //because dto doesn't have id field, set it manually for return
        // we don't need to map product to a new dto because request body dto already has everything except id (set manually)
        return ResponseEntity.ok(productDto);//return 200 ok with the updated product
    }

    //delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);//check if product exists
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);//delete the product
        return ResponseEntity.noContent().build();
    }
}

package com.SpringPractice.store.repositories;

import com.SpringPractice.store.dtos.ProductSummary;
import com.SpringPractice.store.dtos.ProductSummaryDTO;
import com.SpringPractice.store.entities.Category;
import com.SpringPractice.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
//Jpa repository expands on crud giving us some extra features (findAll(example) - finding by example)
public interface ProductRepository extends JpaRepository<Product, Long> , ProductCriteriaRepository{//extend PCR for function
    //Derived Queries/////////////////////////////////////////////////////////////////////////
    // String
    List<Product> findByName(String name);  //select * from products where name = ?
    List<Product> findByNameLike(String name); //select * from products where name like ?
    List<Product> findByNameNotLike(String name);
    List<Product> findByNameContaining(String name);
    List<Product> findByNameStartingWith(String name);
    List<Product> findByNameEndingWith(String name);
    List<Product> findByNameEndingWithIgnoreCase(String name);

    //Numbers
    List<Product> findByPrice(BigDecimal price);//select * from products where price = ?
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByPriceGreaterThanEqual(BigDecimal price);
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    //Null
    List<Product> findByDescriptionNull();
    List<Product> findByDescriptionNotNull();

    //Multiple Conditions
    List<Product> findByDescriptionNullAndNameNull();

    //Sort (OrderBy)
    List<Product> findByNameOrderByPrice(String name);
    List<Product> findByNameOrderByPriceAsc(String name);

    //Limit (Top/First)
    List<Product> findTop5ByNameOrderByPrice(String name);
    List<Product> findFirst5ByNameLikeOrderByPrice(String name);


    //Custom Queries////////////////////////////////////////////////////////////////////////////////////////////////
    //Find products whose prices are in a given range and sort by name
    List<Product> findByPriceBetweenOrderByName(BigDecimal min, BigDecimal max); //Derived Query method (long name)
    //SQL or JPQL (JPQL is simpler and universal across different databases but not as advanced)

    //SQL                                                   parameters require : prefix      SQL query needs native Query
    //@Query(value = "select * from products p where p.price between :min and :max order by p.name", nativeQuery = true)
    //List<Product> findProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
    //                          annotate method params for the min and max

    //JPQL     //JPQL uses entities not tables (Product entity) Ctrl + space
    //@Query("select p from Product p join p.category where p.price between :min and :max order by p.name")
    //JPQL has other features like being able to join with other entities
    //@Query("select p from Product p join p.category where p.price between :min and :max order by p.name")
    //we can also generate this from our derived query with JPA buddy (Alt + Enter, extract JPQL query + named params)
    //List<Product> findProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    //Example of using Stored Procedures (MYSQL version)
    //@Procedure("findProductsByPrice")
    //List<Product> findProducts(BigDecimal min, BigDecimal max);
    //PostgreSQL version
    @Query(value = "SELECT * FROM findProductsByPrice(:min, :max)", nativeQuery = true)
    List<Product> findProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    //counting all products with prices in a range (aggregate functions)
    @Query("select count(*) from Product p where p.price between  :min and :max")
    long countProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    //updating the price of all products in a category (updating data)
    @Modifying //tells hibernate that it is an update operation not select
    @Query("update Product p set p.price = :newPrice where p.category.id = :categoryId")
    void updatePriceByCategory(BigDecimal newPrice, Byte categoryId);


    //example of returning a custom DTO (data transfer object) with JPQL.
    //List<Product> findByCategory(Category category); //fetches all data from prod and cat (eager loading) which may be unnecessary
    //we can return an interface or a class (interface is more flexible and doesn't require a constructor)
    //List<ProductSummary> findByCategory(Category category);//uses dto interface to only select id and name
    //List<ProductSummaryDTO> findByCategory(Category category);//class allows us to encapsulate logic

    //partial data for custom queries
    //@Query("select p from Product p where p.category = :category") //custom query fetches all data from Product and cat
    @Query("select p.id, p.name from Product p where p.category = :category") //need to specify individual fields (id and name) in query
    List<ProductSummary> findByCategory(@Param("category") Category category);

    //when using classes, we need a new instance of the DTO which requires the path (better to use interface, class only if you need logic)
    //@Query("select new com.SpringPractice.store.dtos.ProductSummaryDTO(p.id, p.name) from Product p where p.category = :category")
    //List<ProductSummaryDTO> findByCategory(@Param("category") Category category);
}
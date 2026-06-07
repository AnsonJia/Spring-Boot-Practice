package com.SpringPractice.store.services;

import com.SpringPractice.store.entities.Address;
import com.SpringPractice.store.entities.Category;
import com.SpringPractice.store.entities.Product;
import com.SpringPractice.store.entities.User;
import com.SpringPractice.store.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final EntityManager entityManager; //responsible for managing entities using persistence context
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //example of managing transactions
    @Transactional//changes the transaction boundary to the entire showEntityStates method (persist until method end)
    public void showEntityStates(){
        var user = User.builder()
        		.name("John")
        		.email("john@gmail.com")
        		.password("password")
        		.build();
        //check to see if user is in the persistent state or not
        //no repository method has been called yet so transient (new entity in the transient state)
        if (entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");
        //repository methods are transactional (save, delete, etc). New transaction starts and ends when completed
        userRepository.save(user);//transaction is only active when method is executed. after complete, detach
        //it will be detached because persistence only exists while the transaction is active
        //with @Transactional, it will persist until the method ends so it will be persistent
        if (entityManager.contains(user))
            System.out.println("Persistent");
        else
            System.out.println("Transient / Detached");
    }
    //check to see if eager or lazy loading (Eager: OneToOne, ManyToOne     Lazy: OneToMany, ManyToMany)
    public void showRelatedEntities(){
        var user = userRepository.findById(1L).orElseThrow();
        System.out.println(user.getEmail()); //still prints both user+profile even with lazy loading
    } //in one-to-one relationships, changing fetch strategy only affects owner, so it won't affect the user entity
    //one method to fix this is to remove OneToOne method in User
        // likely best because when fetch user info for authentication, we don't need profile information


    //example of lazy loading
    //user is lazy loaded (when profile transaction ends, cant get user associated to it)
    @Transactional//persistent stays so hibernate can track profile object
    public void showRelatedEntities2(){
        var profile = profileRepository.findById(1L).orElseThrow(); //transaction ends so no persistence
        System.out.println(profile.getBio()); //doesn't load user
        System.out.println(profile.getUser().getEmail());//loads profile+user
    }
    //another example of lazy loading
    public void fetchAddress(){
        //in Address, there is a relationship to user and user has a one-to-one relationship with profile
            //profile will also be eager loaded (address+user+profile) if no lazy loading
        var address = addressRepository.findById(1L).orElseThrow();
    }


    //example of persisting related entities
    public void persistRelated(){
        var user = User.builder()
                .name("John")
                .email("john@gmail.com")
                .password("password")
                .build();
        var address = Address.builder()
                .street("street")
                .city("city")
                .state("state")
                .zip("zip")
                .build();
        user.addAddress(address);
        userRepository.save(user); //only user will be saved not address (hibernate does not propagate to related entities)
        //addressRepository.save(address); //one way to fix this is to save the address separately
            //a better solution is to add cascade persist to the relationship in user so address is propagated
    }


    //example of deleting parent and related entities (user)
    public void deleteRelated(){
        //hibernate first tries to fetch entity with its relationship (no referential integrity violation - data remains in valid state)
        userRepository.deleteById(1L);
        //issue 1: User has a OneToOne relationship with profile and doesn't know what to do with profile if delete user
            //set cascade type to remove in the user relationship
            //many-to-many JOIN tables don't need cascading (hibernate assumes it's safe to delete)
        //issue 2: Address table foreign key doesn't have a delete action (if user delete with address, cant delete user)
            //could set to on delete cascade for db SQL migration, best to set cascade in addresses relation in user
    }
    //example of deleting child entity (addresses)
    @Transactional
    public void deleteRelated2(){
        var user = userRepository.findById(1L).orElseThrow();//findById is transactional. line ends, transaction ends
        var address = user.getAddresses().getFirst();//lazy lading between user and address (need to fetch address but no persistence context)
        user.removeAddress(address);//removeAddress sets user to null (orphan entity), but the db table is set to not null
            //set the addresses relationship in User with orphan removal true
        userRepository.save(user);
    }

    //new product added to new category
    public void newProduct1(){
        var category = new Category("Category 1");// don't need builder since we just need a name
        var product = Product.builder()
                .name("Product 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(10.99))
                .category(category)//category is transient while product is persistent (hibernate doesn't know what to do)
                .build();
        //categoryRepository.save(category);//save category first or add cascade persist in Product class
        productRepository.save(product);//saving product makes product persistent
    }
    //new product added to existing category
    @Transactional//apply transactional so persistent context is open for the whole method
    public void newProduct2(){
        var category = categoryRepository.findById((byte)1).orElseThrow();//transaction starts and ends (detached)
        var product = Product.builder()
                .name("Product 2")
                .description("Description 2")
                .price(BigDecimal.valueOf(10.99))
                .category(category)//no persistence for category because it is now detached
                .build();
        productRepository.save(product);//persistent product references detached category
    }
    @Transactional//keep transaction open
    public void addProductToWishlist(){
        var user = userRepository.findById(1L).orElseThrow(); //transaction starts and ends
        var products = productRepository.findAll(); //transaction starts and ends
        //add all existing products to the user's wishlist
        products.forEach(user::addFavouriteProduct); //no persistence, cant get wishlist from user (wishlist lazy loaded)
        userRepository.save(user);
    }
    public void deleteProduct(){
        productRepository.deleteById(1L);//wishlist table foreign key doesn't have a delete action
        //cannot set delete cascade in wishlist relationship since user is owner, must modify db with on delete cascade
    }
    //testing custom query to update data
    @Transactional//custom query update requires transactional
    public void updateProductPrices(){
        productRepository.updatePriceByCategory(BigDecimal.valueOf(10), (byte) 1);
    }


    //example of fetching partial data with DTO
    public void fetchProducts(){// call find by cat method from the product repository class
        var products = productRepository.findByCategory(new Category((byte)1));//creating a new category by id
        products.forEach(System.out::println);
    }
    //@Transactional //Mysql using @Procedure (in Product Repository) requires a transactional
    //Example of using stored procedures/functions
    public void fetchProducts2(){// call find by cat method from the product repository class
        var products = productRepository.findProducts(BigDecimal.valueOf(1), BigDecimal.valueOf(15));//creating a new category by id
        products.forEach(System.out::println);
    }

    //example of dynamic queries with query by example
    @Transactional
    public void fetchProducts3(){
        var product = new Product();
        // set fields we want to use to finding products
        product.setName("Product"); //PostgreSQL is case-sensitive, MySQL is not
        //create a matching object to customize the matching behavior
        var matcher = ExampleMatcher.matching() //returns an ExampleMatcher object like a Builder object
                .withIncludeNullValues() //show fields with null values (usually ignored)
                .withIgnorePaths("id", "description")// we can exclude fields
                .withIgnoreCase() //ignore case
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);//string matcher containing the example name (like operator)
        //create an example product with the product name
        var example = Example.of(product, matcher); //default will use exact matching. Matcher allows custom matching
        //pass example to repository to find all products that are similar to the example product (needs JpaRepository)
        var products = productRepository.findAll(example);//get products that match example
        products.forEach(System.out::println);
        //limitation:
        //no nested properties, collections/maps, string matching is database specific
        // only exact matching for all other types (num/dates, etc) cant find price within a range
    }

    //example of using EntityGraph to efficiently load entities
    @Transactional//the to string in User will look at every field including lazy loaded fields (override toString)
    public void fetchUser(){
        var user = userRepository.findByEmail("email@gmail.com").orElseThrow();//transaction starts and ends
        System.out.println(user); //to string tries to access lazy loaded fields but no persistence
    }
    //example of efficiently lazy loading (n+1 problem)
    @Transactional
    public void fetchUsers(){
        //var users = userRepository.findAll(); )
        var users = userRepository.findAllWithAddresses();
        users.forEach(u -> {
            System.out.println(u);//will also query each user profile because of the relationship (remove relation in User)
            //n+1 problem: 1 query to fetch all users, then n queries to fetch addresses for each user due to lazy loading
            u.getAddresses().forEach(System.out::println); //can solve n+1 using eager loading with EntityGraph in userRepo
        });
    }
    //practice for writing custom queries
    @Transactional
    public void printLoyalProfiles(){ //basics using derived query
        var profiles = profileRepository.findByLoyaltyPointsGreaterThanOrderByUserEmail(2);
        profiles.forEach(p-> System.out.println(p.getId() + ": " + p.getUser().getEmail()));
    }
    @Transactional
    public void printLoyalProfiles2(){ //using custom query and dto interface
        var profiles = profileRepository.findLoyalProfiles(2);
        profiles.forEach(p-> System.out.println(p.getId() + ": " + p.getEmail()));
    }
    @Transactional
    public void printLoyalProfiles3(){ //moved method to UserRepository
        var users = userRepository.findLoyalUsers(2);
        users.forEach(p-> System.out.println(p.getId() + ": " + p.getEmail()));
    }
}

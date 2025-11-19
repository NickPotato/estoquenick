package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; //haha beans (I'll get to autowired in a second)
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.estoquenick.dto.ProductRequest;
import com.estoquenick.dto.ProductResponse;
import com.estoquenick.repository.ProductRepo;
import com.estoquenick.repository.CategoryRepo;
import com.estoquenick.model.Product;
import com.estoquenick.model.Category;

import java.util.List;

//@Service is for creating a "bean", which is an object of this class, and lets it be injected somewhere else i think
//Also it's important for connecting Repository and Controller
@Service
public class ProductService {

    //Autowired is to automatically inject an instance of an object, I think, in this case productRepository, cuz ProductRepo is actually also a Spring Bean (that's a funny name)
    //Basically I only type in the structure, let Spring do all the work and then inject the bean in here with Autowired :3
    @Autowired 
    private ProductRepo productRepository;

    @Autowired
    private CategoryRepo categoryRepository;

    //list everything
    public List<ProductResponse> findAll() {
        return productRepository.findAll() //note this returns a list i think
            .stream() //convert into usable stuff 
            .map(this::toResponse) //map it properly, but not before converting it to dto
            .toList(); //now make a list out of it
    }

    //search using id
    public ProductResponse findById(Long id) { //findById is likely something spring added automatically, since it wasn't me lol
        Product product = productRepository.findById(id) //attempt to find the desired product using its id
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return toResponse(product); //if found, return product in dto form
    }

    // public Product save(Product product) {
    //     //here are all the failsafes being put in place
    //     //obsolete now that we're using dto, value limits are already in place
    //    
    //     //needs a name:
    //     if (product.getName() == null || product.getName().isBlank()) {
    //         throw new RuntimeException("Product needs a name");
    //     }
    //     //price has to be at least more than nothing lol
    //     if (product.getPrice() <= 0 || product.getPrice() == null) {
    //         throw new RuntimeException("Price can't be zero or negative");
    //     }
    //     //stock can't be negative
    //     if (product.getCurrentStock() == null || product.getCurrentStock() < 0) {
    //         throw new RuntimeException("Stock can't be negative");
    //     }
    //     //category must be inserted
    //     if (product.getCategory() == null || product.getCategory().getId() == null) {
    //         throw new RuntimeException("Valid category required");
    //     }
    //     //category inserted must actually exist lmao, we're checking the databank for this one
    //     categoryRepository.findById(product.getCategory().getId()) //check find the id of the current category
    //         .orElseThrow(() -> new RuntimeException("Category does not exist"));
    //
    //     return productRepository.save(product); //returns the saved object? this deals with both Inserting AND Updating so that's pretty darn cool
    // }

    //CREATE the product, wow i put it in all caps this time!
    public ProductResponse save(ProductRequest dto) {

        Category category = categoryRepository.findById(dto.categoryId())  //check the category id to see if it exists
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        Product p = new Product(); //set the current product's properties to the dto we have on hand (plus the category)
        p.setName(dto.name());
        p.setPrice(dto.price());
        p.setCurrentStock(dto.currentStock());
        p.setCategory(category);

        productRepository.save(p); //and then save it
        return toResponse(p); //and return the product we just created
    }

    public void delete(Long id) {
        productRepository.deleteById(id); //also self explanatory
    }

    //guess what this one does lmao, ding ding ding that's right! updates existing values!
    public ProductResponse update(Long id, ProductRequest dto) {

        Product p = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        p.setName(dto.name()); //just like before, set the new product to the dto properties we have on hand
        p.setPrice(dto.price());
        p.setCurrentStock(dto.currentStock());
        p.setCategory(category);

        productRepository.save(p); //save and return new product
        return toResponse(p);
    }

    //this converts entities into dto format!!
    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCurrentStock(),
                p.getCategory() != null ? p.getCategory().getName() : null //if the category exists then gets its name, otherwise do nothing
        );
    }
}

package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; //haha beans (I'll get to autowired in a second)

import com.estoquenick.repository.ProductRepo;
import com.estoquenick.model.Product;

import java.util.List;

//@Service is for creating a "bean", which is an object of this class, and lets it be injected somewhere else i think
//Also it's important for connecting Repository and Controller
@Service
public class ProductService {

    //Autowired is to automatically inject an instance of an object, I think, in this case productRepository, cuz ProductRepo is actually also a Spring Bean (that's a funny name)
    //Basically I only type in the structure, let Spring do all the work and then inject the bean in here with Autowired :3
    @Autowired 
    private ProductRepo productRepository;

    public List<Product> findAll() {
        return productRepository.findAll(); //note this returns a list i think
    }

    public Product findById(Long id) { //findById is likely something spring added automatically, since it wasn't me lol
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("O produto não foi encontrado")); //self explanatory
    }

    public Product save(Product product) {
        return productRepository.save(product); //returns the saved object? this deals with both Inserting AND Updating so that's pretty darn cool
    }

    public void delete(Long id) {
        productRepository.deleteById(id); //also self explanatory
    }
}

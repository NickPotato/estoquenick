package com.estoquenick.controller;

import com.estoquenick.model.Product;
import com.estoquenick.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //self explanatory, that's what this class is
@RequestMapping("/api/products") //this is something for the http thing to handle apparently, gotcha
public class ProductController {

    @Autowired //autowired explained in productservice already, go check that for a refresher!
    private ProductService productService;

    @GetMapping //get, post, put, deletemapping are all http stuff
    public List<Product> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) { //@PathVariable = /api/products/{id} i believe
        return productService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED) //changes current http status?
    @PostMapping
    public Product save(@RequestBody Product product) {
        // validate rules before saving here
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product atualizar(@PathVariable Long id, @RequestBody Product product) {
        // optionally check if it's actually the same id, im prolly not going to tho lol
        product.setId(id);
        return productService.save(product);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}

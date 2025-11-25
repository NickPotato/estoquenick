package com.estoquenick.controller;

import com.estoquenick.service.ProductService;
import com.estoquenick.dto.ProductRequest;
import com.estoquenick.dto.ProductResponse;
import com.estoquenick.dto.PriceAdjustmentRequest;
import com.estoquenick.dto.PriceMassAdjustmentRequest;

import com.estoquenick.dto.PriceListResponse;
import com.estoquenick.dto.LowStockResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //self explanatory, that's what this class is
@RequestMapping("/api/products") //this is where the requests for the client are handled at, i think
public class ProductController {

    @Autowired //autowired explained in productservice already, go check that for a refresher!
    private ProductService productService;

    @GetMapping //get, post, put, deletemapping are all http stuff
    public List<ProductResponse> findAll() { //gets everything
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) { //@PathVariable = /api/products/{id} i believe
        return productService.findById(id); //basically this lets us see a specific product by checking from their id
    }

    //---- report mappings start here
    @GetMapping("/reports/price-list")
    public List<PriceListResponse> getPriceList() {
        return productService.getPriceList(); //goes get the pricelist from service, nothin too fancy
    }

    @GetMapping("/reports/low-stock")
    public List<LowStockResponse> getLowStockReport() {
        return productService.getLowStockReport(); 
    }
    //---- report mappings end here

    @ResponseStatus(HttpStatus.CREATED) //changes current http status
    @PostMapping
    public ProductResponse save(@RequestBody @Valid ProductRequest dto) { //receives a ProductRequest, passes that over to service, which registers it properly n all
        return productService.save(dto); 
    }

    //update is similar to save, difference is it takes an existing id and uses put instead of post
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @RequestBody @Valid ProductRequest dto) {
        return productService.update(id, dto);
    }

    //adjust the price for a specific item
    @PutMapping("/{id}/adjust-price")
    public ProductResponse adjustPrice(@PathVariable Long id, @RequestBody PriceAdjustmentRequest request) {
        return productService.adjustPrice(id, request.percentage()); 
        //gets the id, shoots it over to service to handle the pricing change
    }

    //adjust the price for an entire category (or our entire catalog, go ham)
    @PutMapping("/adjust-price")
    public List<ProductResponse> adjustPriceMass(@RequestBody PriceMassAdjustmentRequest request) { //i hate lists
        return productService.adjustPriceMass(request.percentage(), request.categoryId());
        //same as normal price adjustment
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}

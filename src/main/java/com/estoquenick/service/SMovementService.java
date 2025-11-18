package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.estoquenick.model.StockMovement;
import com.estoquenick.model.Product;
import com.estoquenick.model.MovementType;
import com.estoquenick.repository.ProductRepo;
import com.estoquenick.repository.SMovementRepo;

import java.time.LocalDateTime;
import java.util.List;

//apparently this one is a little different than the others in a way, i'll see how it goes
//also check productserv for more info
@Service
public class SMovementService {

    @Autowired
    private SMovementRepo stockMovementRepository;

    @Autowired
    private ProductRepo productRepository;

    //easy find everything lol
    public List<StockMovement> findAll() {
        return stockMovementRepository.findAll();
    }

    public StockMovement findById(Long id) {
        return stockMovementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movement not found"));
    }

    // stock entry
    public StockMovement registerEntry(StockMovement movement) {

        //doesnt allow trying to add nothing, or less than nothing
        if (movement.getQuantity() <= 0 || movement.getQuantity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be above 0");
        }

        //get the product it's looking for, and check if it actually exists
        Product product = getProductOrThrow(movement);

        movement.setType(MovementType.ENTRY);
        movement.setDateAndTime(LocalDateTime.now()); //make sure it's got the proper one lol

        //update current stock, add in however much was added by the request
        product.setCurrentStock(product.getCurrentStock() + movement.getQuantity());
        productRepository.save(product);

        movement.setProduct(product); //make SURE it's returning the FULL product, and not just an id
        return stockMovementRepository.save(movement);
    }

    // stock exit, make sure there's at least something in stock
    public StockMovement registerExit(StockMovement movement) {

        //doesn't allow trying to take out nothing, or negative amounts of something
        if (movement.getQuantity() <= 0 || movement.getQuantity() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be above 0");
        }

        Product product = getProductOrThrow(movement);

        //if it's attempting to take out more than what we have in stock, throw an error
        if (product.getCurrentStock() < movement.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempting to take out more than what's available in stock");
        }

        movement.setType(MovementType.EXIT);
        movement.setDateAndTime(LocalDateTime.now());

        //take out however much was requested of the current stock for the chosen product
        product.setCurrentStock(product.getCurrentStock() - movement.getQuantity());
        productRepository.save(product);

        movement.setProduct(product);
        return stockMovementRepository.save(movement);
    }

    // extra method to check if the product actually exists
    private Product getProductOrThrow(StockMovement movement) {
        if (movement.getProduct() == null || movement.getProduct().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movement must contain product.id");
        }

        //return the product that was found (if it was found), otherwise throw an error
        return productRepository.findById(movement.getProduct().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public void delete(Long id) {
        stockMovementRepository.deleteById(id);
    }
}

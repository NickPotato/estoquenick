package com.estoquenick.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.estoquenick.dto.SMovementRequest;
import com.estoquenick.dto.SMovementResponse;
import com.estoquenick.model.StockMovement;
import com.estoquenick.model.Product;
import com.estoquenick.model.MovementType;
import com.estoquenick.repository.ProductRepo;
import com.estoquenick.repository.SMovementRepo;

// import java.time.LocalDateTime; //not needed anymore as we're using dto
import java.util.List;

//apparently this one is a little different than the others in a way, i'll see how it goes
//still, check productserv for more info
@Service
public class SMovementService {

    @Autowired
    private SMovementRepo stockMovementRepository;

    @Autowired
    private ProductRepo productRepository;

    //easy find everything lol
    public List<SMovementResponse> findAll() {
        return stockMovementRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public SMovementResponse findById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id) //find movement via its id
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movement not found"));

        return toResponse(movement); //return in dto form
    }

    // stock entry
    public SMovementResponse registerEntry(SMovementRequest req) {
        Product product = getProductOrThrow(req.productId()); 

        //section that updates its own stock
        product.setCurrentStock(product.getCurrentStock() + req.quantity()); //current stock will alwyas be 0 on register, but it's nice to have failsafes anyway
        productRepository.save(product); //save, ofc

        //now we gotta save the movement itself:
        StockMovement movement = new StockMovement();
        movement.setType(MovementType.ENTRY);
        movement.setQuantity(req.quantity());
        movement.setProduct(product); //gotta set the product itself too lol   

        stockMovementRepository.save(movement); //save the movement that just happened

        return toResponse(movement); //return said movement as a dto
    }

    // stock exit, make sure there's at least something in stock
    // apologies for the comments being more monotone down here, i already commeted stuff up on registerEntry lol
    public SMovementResponse registerExit(SMovementRequest req) {
        Product product = getProductOrThrow(req.productId());

        if (product.getCurrentStock() < req.quantity()) { //if it's trying to take more than we have, then:
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for exit");
        }

        // stock exit
        product.setCurrentStock(product.getCurrentStock() - req.quantity());
        productRepository.save(product);

        // save movement
        StockMovement movement = new StockMovement();
        movement.setType(MovementType.EXIT);
        movement.setQuantity(req.quantity());
        movement.setProduct(product);

        stockMovementRepository.save(movement);

        return toResponse(movement);
    }
    //helpers down here ↓

    // extra method to check if the product actually exists
    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id) //return the product found using its id, if it actually finds anything
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    //converts into dto
    private SMovementResponse toResponse(StockMovement m) {
        return new SMovementResponse(
                m.getId(),
                m.getType(),
                m.getQuantity(),
                m.getDateAndTime(),
                m.getProduct().getId(),
                m.getProduct().getName()
        );
    }

    public void delete(Long id) {
        stockMovementRepository.deleteById(id);
    }
}

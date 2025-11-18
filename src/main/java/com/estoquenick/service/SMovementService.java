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
            .orElseThrow(() -> new RuntimeException("Movimento não encontrado"));
    }

    // stock entry
    public StockMovement registerEntry(StockMovement movement) {
        Product product = getProductOrThrow(movement);

        movement.setType(MovementType.ENTRY);

        product.setCurrentStock(product.getCurrentStock() + movement.getQuantity());
        productRepository.save(product);

        return stockMovementRepository.save(movement);
    }

    // stock exit, make sure there's at least something in stock
    public StockMovement registerExit(StockMovement movement) {
        Product product = getProductOrThrow(movement);

        if (product.getCurrentStock() < movement.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Estoque insuficiente para saída"
            );
        }

        movement.setType(MovementType.EXIT);

        product.setCurrentStock(product.getCurrentStock() - movement.getQuantity());
        productRepository.save(product);

        return stockMovementRepository.save(movement);
    }

    // extra method to check if the product actually exists
    private Product getProductOrThrow(StockMovement movement) {
        if (movement.getProduct() == null || movement.getProduct().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Movimentação precisa conter product.id"
            );
        }

        return productRepository.findById(movement.getProduct().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

     public void delete(Long id) {
        stockMovementRepository.deleteById(id);
    }
}

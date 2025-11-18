package com.estoquenick.controller;

import com.estoquenick.model.StockMovement;
import com.estoquenick.service.SMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//check product controller for more info
//note, we're changing this soon so that Product.currentStock is automatically messed with

@RestController
@RequestMapping("/api/movement")
public class SMovementController {

    @Autowired
    private SMovementService movementService;

    @GetMapping
    public List<StockMovement> findAll() {
        return movementService.findAll();
    }

    // Entry
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/entry")
    public StockMovement registerEntry(@RequestBody StockMovement movement) {
        // saves directly for NOW, we'll change this up soon I think
        return movementService.registerEntry(movement);
    }

    // Exit
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/exit")
    public StockMovement registerExit(@RequestBody StockMovement movement) {
        // marks type as exit and validates something im not sure
        return movementService.registerExit(movement);
    }

    @GetMapping("/{id}")
    public StockMovement findById(@PathVariable Long id) {
        return movementService.findById(id);
    }
}
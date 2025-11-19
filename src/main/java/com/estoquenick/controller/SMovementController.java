package com.estoquenick.controller;

import com.estoquenick.service.SMovementService;
import com.estoquenick.dto.SMovementRequest;
import com.estoquenick.dto.SMovementResponse;

import jakarta.validation.Valid;

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
    public List<SMovementResponse> findAll() {
        return movementService.findAll();
    }

    @GetMapping("/{id}")
    public SMovementResponse findById(@PathVariable Long id) {
        return movementService.findById(id);
    }

    // Entry
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/entry")
    public SMovementResponse registerEntry(@RequestBody @Valid SMovementRequest req) {
        // saves directly for NOW, we'll change this up soon I think
        return movementService.registerEntry(req);
    }

    // Exit
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/exit")
    public SMovementResponse registerExit(@RequestBody SMovementRequest req) {
        // marks type as exit and validates something im not sure
        return movementService.registerExit(req);
    }
}
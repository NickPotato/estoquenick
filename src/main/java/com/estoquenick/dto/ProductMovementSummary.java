package com.estoquenick.dto;

//this is for a single product, and the sum of its movements (entries, exits)
public record ProductMovementSummary(
    Long productId,
    String productName,
    long totalQuantity
) {}

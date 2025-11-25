package com.estoquenick.dto;

public record LowStockResponse(
    String productName,
    Integer currentStock,
    Integer minStock
) 
{}
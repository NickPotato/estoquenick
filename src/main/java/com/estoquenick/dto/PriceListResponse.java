package com.estoquenick.dto;

public record PriceListResponse(
    String productName,
    Double unitPrice,
    String unit,
    String categoryName
) 
{}

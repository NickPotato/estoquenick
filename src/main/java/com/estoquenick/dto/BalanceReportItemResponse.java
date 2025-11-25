package com.estoquenick.dto;

//we're separating this from normal itemResponse for less clutter, and it looks neater :D
public record BalanceReportItemResponse(
    String productName,
    Integer quantity,
    Double price,
    Double totalValue
) {}

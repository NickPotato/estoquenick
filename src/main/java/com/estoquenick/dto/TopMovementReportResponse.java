package com.estoquenick.dto;

//THIS is the one we'll use for the report
public record TopMovementReportResponse(
    ProductMovementSummary topEntryProduct,
    ProductMovementSummary topExitProduct
) {}

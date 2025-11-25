package com.estoquenick.dto;

import java.util.List;
//no need to import itemResponse since it's in the same package already

//this, unlike itemResponse is the FULL report, and it'll be what we're gonna use on the Get endpoint
public record BalanceReportResponse(
    List<BalanceReportItemResponse> items,
    double totalStockValue //this is for our entire catalogue
) {}
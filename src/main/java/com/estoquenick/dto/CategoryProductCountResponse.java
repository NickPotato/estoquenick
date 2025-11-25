package com.estoquenick.dto;

//easiest one?
public record CategoryProductCountResponse(
    String categoryName,
    long productCount
) {}

package com.estoquenick.dto;

import com.estoquenick.model.enums.CategorySize;
import com.estoquenick.model.enums.CategoryPackage;

public record CategoryResponse(
    Long id,
    String name,
    CategorySize size,
    CategoryPackage packageType
) {}

//easy :D
package com.estoquenick.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.estoquenick.model.enums.CategorySize;
import com.estoquenick.model.enums.CategoryPackage;

public record CategoryRequest(
    @NotBlank String name, //none of these can be empty
    @NotNull CategorySize size,
    @NotNull CategoryPackage packageType
) {}

package com.estoquenick.dto;

import jakarta.validation.constraints.NotNull;

public record PriceAdjustmentRequest(
    @NotNull Double percentage // gotta specify a percentage for it to work bo
) {}

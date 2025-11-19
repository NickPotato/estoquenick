package com.estoquenick.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SMovementRequest(
    @NotNull Long productId,
    @NotNull @Min(value = 1) Integer quantity //must add or remove at least 1 item
) 
{}

package com.estoquenick.dto;

import jakarta.validation.constraints.NotNull;

public record PriceMassAdjustmentRequest(
    @NotNull Double percentage,
    Long categoryId //this is apparently optional? how come?
) {}

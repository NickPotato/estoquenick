package com.estoquenick.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank String name //category name cannot be empty
) {}

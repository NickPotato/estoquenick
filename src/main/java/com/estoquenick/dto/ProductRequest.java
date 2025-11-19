package com.estoquenick.dto;

import jakarta.validation.constraints.Min; //Element must be higher than the specified number when this is used
import jakarta.validation.constraints.NotBlank; //this and the bottom one are self explanatory
import jakarta.validation.constraints.NotNull;

//This is the bit that, well, requests info for the client (postman/frontend), pResponse deals with the output I believe
public record ProductRequest(
    @NotBlank String name,
    @NotNull @Min(0) Double price, //I feel like the minimum price here should be 1, but we have to account for cents too
    @NotNull @Min(0) Integer currentStock,
    @NotNull Long categoryId
) {} //this is here because it's instantly opening and closing a function, even if it doesn't look like it lol

package com.estoquenick.dto;

//This is what we'll use to send the report back to our client (postman in this case, but the front-end application later on)
public record ProductResponse (
    Long id,
    String name,
    Double price,
    Integer currentStock,
    String categoryName //Note that it won't return the entire category, just the name, okie?
) {} //these types of records are funny, not a single thing in the function bit, huh?
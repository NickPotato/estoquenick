package com.estoquenick.dto;

import com.estoquenick.model.MovementType;
import java.time.LocalDateTime; //i need this JUST for the variable type to work lmao

public record SMovementResponse(
    Long id,
    MovementType type,
    Integer quantity,
    LocalDateTime dateAndTime,
    Long productId,
    String productName
) {}

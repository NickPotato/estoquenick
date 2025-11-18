package com.estoquenick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estoquenick.model.StockMovement;

public interface SMovementRepo extends JpaRepository<StockMovement, Long> {
    //more info in ProductRepo
}

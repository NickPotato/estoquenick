package com.estoquenick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estoquenick.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
//more info in ProductRepo
}

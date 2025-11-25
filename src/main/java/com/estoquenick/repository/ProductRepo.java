package com.estoquenick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estoquenick.model.Product;
import java.util.List;

//So Apparently, Spring Data JPA automatically implements everything I'll need, so I guess I just leave most of these empty for now, but I'll see what ends up happening in this thing later
//Btw arguments for this thing are: JpaRepository<Class, PrimaryKeyType>

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
}

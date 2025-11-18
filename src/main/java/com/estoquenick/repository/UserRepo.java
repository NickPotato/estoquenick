package com.estoquenick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.estoquenick.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    //more info in ProductRepo
}

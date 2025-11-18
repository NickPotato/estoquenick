package com.estoquenick.model;

import jakarta.persistence.*;
import lombok.Data;

// user authentication, standard stuff
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //self explanatory i hope lol
    private String username;

    @Column(nullable = false)
    private String password; //will undergo encryption dw dw

    @Column(nullable = false)
    private String role; //role can be either ROLE_USER or ROLE_ADMIN, I'm assuming this will be a constant later on
}

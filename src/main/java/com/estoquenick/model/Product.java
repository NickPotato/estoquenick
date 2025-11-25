package com.estoquenick.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank; //these two weren't originally here but I like making sure

import jakarta.persistence.*;
import lombok.Data;

/* As far as my understanding goes (and let's hope I'm right), there will be multiple products, and each of them will have a corresponding value assigend to them (those being the variables defined here)

Cool! Now, how does all of this fit into a table, exactly...? 
Because I know jakarta is doing something table-related with all of those Columns and IDs and primary keys and whatnot, yeah? */

// Each product belongs to a category, and it has control over stock I think
@Entity
@Data
public class Product {
    //so ID and GeneratedValue create a primary key, this is the unique column that identifies the product
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank
    @Column(nullable = false) //This one doesn't have to be unique, unlike Category, huh?
    private String name;

    @NotNull
    @Column(nullable = false)
    private Double price; //reminder you'll need a way to change this in bulk later

    @NotNull
    @Column(nullable = false)
    private Integer currentStock; //THIS is the main value that will be altered during the whole process and whatnot
    
    @ManyToOne //so that multiple products can be part of the same category
    @JoinColumn(name = "category_id", nullable = false) //defining the "FK" (foregin key) column in the database, remember this name for later
    private Category category; //this feels so wrong, but I'm pretty sure we're assigning each product to a category w/ this

    @NotNull
    @Column(nullable = false)
    private Integer minStock;

    @NotNull
    @Column(nullable = false)
    private Integer maxStock;

    @NotBlank
    @Column(nullable = false)
    private String unit;

    /*I think that foreign keys are like "children" to primary keys so that there's no objects pointing to data that doesn't exist, yeah?
    this is called referential integrity, I believe, thanks google! */
}

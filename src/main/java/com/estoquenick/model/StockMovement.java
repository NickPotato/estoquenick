package com.estoquenick.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime; //we'll need this!

/* ty vs code for teaching me multiline comments <3
 * Anyway, this class is for registering every movement (entry and exit) of our stock items
 * I'm thinking we could also like, register every price change, but let's leave that for later if there's still time left
 */

@Entity
@Data
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type; //yet another import from another class

    @Column(nullable = false)
    private Integer quantity; //I'm not sure whether to name this "amount" or "quantity"

    //it's funny how self explanatory and simple this one is compared to the rest lol
    @Column(nullable = false)
    private LocalDateTime dateAndTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "product_id") //make sure not to forget names, buddy!
    private Product product; //we're doing this again, huh
}

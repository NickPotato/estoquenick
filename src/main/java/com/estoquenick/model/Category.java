package com.estoquenick.model;

//adding these two as we're using set types
import jakarta.persistence.*; //Used for the whole 'object relational mapping' thing or whatever
import lombok.Data; //auto gets and sets, please and thank you
import jakarta.validation.constraints.NotNull;

import com.estoquenick.model.enums.CategorySize;
import com.estoquenick.model.enums.CategoryPackage;

@Entity //apparently this transforms this class into a table value...?
@Data //lombok thing
//This class only concerns itself with grouping products together, I believe
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //gotta be unique to prevent duplicate categories lol
    private String name;

    //I'm fairly certain we don't need @NotNull since our column's nullable is already false, but I wanna make sure
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorySize size;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryPackage packageType;
}

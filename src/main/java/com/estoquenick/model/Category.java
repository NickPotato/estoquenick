package com.estoquenick.model;

import jakarta.persistence.*; //Used for the whole 'object relational mapping' thing or whatever
import lombok.Data; //auto gets and sets, please and thank you

@Entity //apparently this transforms this class into a table value...?
@Data //lombok thing
//This class only concerns itself with grouping products together, I believe
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //gotta be unique to prevent duplicate categories lol
    private String name;
}

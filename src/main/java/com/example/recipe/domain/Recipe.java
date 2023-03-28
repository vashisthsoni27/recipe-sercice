package com.example.recipe.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Recipe contains all attributes of a dish.
 *
 * Created by Vashisth Soni
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="recipe")
public class Recipe implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column
    private String name;

    @Column
    private String instructions;

    @Column
    private boolean containsMeat;

    @Column
    private boolean isVegan;

    @Column
    private Integer noOfServings;

    @OneToMany(mappedBy = "recipe", cascade =  CascadeType.ALL)
    private List<Ingredient> ingredients;

    public Recipe(String name, String instructions, boolean containsMeat, boolean isVegan, Integer noOfServings, List<Ingredient> ingredients) {
        this.name = name;
        this.instructions = instructions;
        this.containsMeat = containsMeat;
        this.isVegan = isVegan;
        this.noOfServings = noOfServings;
        this.ingredients = ingredients;
    }


}
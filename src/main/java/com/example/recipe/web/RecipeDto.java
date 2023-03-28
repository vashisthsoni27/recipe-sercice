package com.example.recipe.web;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.recipe.domain.Ingredient;
import lombok.Getter;
import lombok.Setter;

import org.springframework.hateoas.ResourceSupport;

/**
 * Data Transfer Object for Ingredient.
 *
 * Created by Vashisth Soni
 */
@Getter
@Setter
public class RecipeDto extends ResourceSupport {

    @NotNull
    @Size(min=3, max = 255)
    private String name;

    @NotNull
    @Size(min=20, max = 1000)
    private String instructions;

    @NotNull
    private boolean containsMeat;

    @NotNull
    private boolean isVegan;

    @NotNull
    private Integer noOfServings;

    List<Ingredient> ingredients;
    /**
     * Constructor to fully initialize the RecipeDto
     *
     * @param name name
     * @param details instructions
     * @param containsMeat containsMeat
     * @param isVegan isVegan
     * @param noOfServings noOfServings
     */
    public RecipeDto(String name, String details, Boolean containsMeat, Boolean isVegan, Integer noOfServings) {
        this.name = name;
        this.instructions = details;
        this.containsMeat = containsMeat;
        this.isVegan = isVegan;
        this.noOfServings = noOfServings;
    }
}
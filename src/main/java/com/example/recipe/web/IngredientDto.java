package com.example.recipe.web;

import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
public class IngredientDto extends ResourceSupport {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String measurement;

    /**
     * Constructor to fully initialize the IngredientDto
     *
     * @param name name
     * @param measurement measurement
     */
    public IngredientDto(String name, String measurement) {
        this.name = name;
        this.measurement = measurement;
    }
}
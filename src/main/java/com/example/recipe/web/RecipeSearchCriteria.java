package com.example.recipe.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeSearchCriteria {
    private boolean vegetarian;
    private Long serves;
    private String excludeIngredients;
    private String includeInstructions;
    private String ingredientName;
}
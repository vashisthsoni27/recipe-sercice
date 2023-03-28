package com.example.recipe.web;

import com.example.recipe.domain.Ingredient;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Ingredient Assembler, convert Ingredient to a Hateoas Supported Ingredient class
 *
 * Created by Vashisth Soni.
 */
@Component
public class IngredientAssembler extends ResourceAssemblerSupport<Ingredient,IngredientDto> {
    private final RepositoryEntityLinks entityLinks;

    public IngredientAssembler( RepositoryEntityLinks entityLinks) {
        super(RecipeIngredientController.class, IngredientDto.class);
        this.entityLinks = entityLinks;
    }

    /**
     *  Generates "self", "ingredient" and recipe links
     *
     * @param ingredient ingredient Entity
     * @return ingredientDto ingredientDto
     */
    @Override
    public IngredientDto toResource(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto(ingredient.getName(), ingredient.getMeasurement());

        Link ingredientLink = entityLinks.linkToSingleResource(Ingredient.class, ingredient.getIngredientId());
        ingredientDto.add(ingredientLink.withRel("ingredient"));
        return ingredientDto;
    }

}
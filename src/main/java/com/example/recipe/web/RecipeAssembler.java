package com.example.recipe.web;

import com.example.recipe.domain.Recipe;

import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Recipe Assembler, convert Recipe to a Hateoas Supported Recipe class
 *
 * Created by vashisthsoni.
 */
@Component
public class RecipeAssembler extends ResourceAssemblerSupport<Recipe,RecipeDto> {

    private final RepositoryEntityLinks entityLinks;

    public RecipeAssembler( RepositoryEntityLinks entityLinks) {
        super(RecipeController.class, RecipeDto.class);
        this.entityLinks = entityLinks;
    }

    /**
     *  Generates "self", "ingredient" and recipe links
     *
     * @param recipe recipe Entity
     * @return recipeDto recipeDto
     */
    @Override
    public RecipeDto toResource(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto(recipe.getName(), recipe.getInstructions(), recipe.isVegan(), recipe.isContainsMeat(), recipe.getNoOfServings());

        //"recipe" : ".../recipes/{recipeId}"
        Link recipeLink = entityLinks.linkToSingleResource(Recipe.class, recipe.getRecipeId());
        recipeDto.add(recipeLink.withRel("recipe"));
        return recipeDto;
    }

}
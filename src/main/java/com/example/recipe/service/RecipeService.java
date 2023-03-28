package com.example.recipe.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repo.RecipeRepository;
import com.example.recipe.web.RecipeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Recipe Service
 *
 * Created by Vashisth Soni.
 */
@Service
public class RecipeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);
    private final RecipeRepository recipeRepository;

    /**
     * Construct RecipeService
     *
     * @param recipeRepository Recipe Repository
     */
    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Create a recipe in the database
     * @param recipeDto recipeDto
     * @throws NoSuchElementException if no Recipe found.
     */
    public void createNew(RecipeDto recipeDto) throws NoSuchElementException {
        LOGGER.info("Create new recipe name {}", recipeDto.getName());
        recipeRepository.save(new Recipe(recipeDto.getName(), recipeDto.getInstructions(), recipeDto.isContainsMeat(), recipeDto.isVegan(), recipeDto.getNoOfServings(), recipeDto.getIngredients()));
    }

    /**
     * Get a recipe by id.
     *
     * @param id racipe identifier
     * @return Recipe
     */
    public Optional<Recipe> lookupRecipeById(Long id)  {
        return recipeRepository.findById(id);
    }

    /**
     * Get a page of recipe for a recipe.
     *
     * @param recipeId identifier
     * @param pageable page parameters to determine which elements to fetch
     * @return Page of Recipes
     * @throws NoSuchElementException if no Recipe found.
     */
    public Page<Recipe> lookupRecipe(long recipeId, Pageable pageable) throws NoSuchElementException  {
        LOGGER.info("Lookup Recipe {}", recipeId);
        return recipeRepository.findByRecipeId(verifyRecipe(recipeId).getRecipeId(), pageable);
    }

    /**
     * Update some of the elements of a Recipe Ingredient.
     *
     * @param recipeId recipe identifier
     * @param recipeDto recipeDto
     * @throws NoSuchElementException if no recipe found.
     */
    public Recipe update(Long recipeId, RecipeDto recipeDto) throws NoSuchElementException {
        LOGGER.info("Update recipe {}", recipeId);
        Recipe recipe = verifyRecipe(recipeId);
        recipe.setName(recipeDto.getName());
        recipe.setInstructions(recipeDto.getInstructions());
        recipe.setContainsMeat(recipeDto.isContainsMeat());
        recipe.setVegan(recipeDto.isVegan());
        recipe.setNoOfServings(recipeDto.getNoOfServings());
        return recipeRepository.save(recipe);
    }

    /**
     * Delete an recipe.
     *
     * @param recipeId identifier
     * @throws NoSuchElementException if no recipe found.
     */
    public void deleteById(Long recipeId) throws NoSuchElementException {
        LOGGER.info("Delete Recipe {}", recipeId);
        Recipe recipe = verifyRecipe(recipeId);
        recipeRepository.delete(recipe);
    }

    /**
     * Verify and return the recipe given a recipeId.
     *
     * @param recipeId recipeId
     * @return the found Recipe
     * @throws NoSuchElementException if no Recipe found.
     */
    public Recipe verifyRecipe(Long recipeId) throws NoSuchElementException {
        return recipeRepository.findById(recipeId).orElseThrow(() ->
                new NoSuchElementException("Recipe does not exist " + recipeId)
        );
    }
}
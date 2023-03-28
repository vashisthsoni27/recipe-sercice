package com.example.recipe.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repo.IngredientRepository;
import com.example.recipe.repo.RecipeRepository;
import com.example.recipe.web.IngredientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Ingredient Service
 *
 * Created by Vashisth Soni.
 */
@Service
public class IngredientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    /**
     * Construct IngredientService
     *
     * @param ingredientRepository Ingredient Repository
     * @param recipeRepository Recipe Repository
     */
    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Create a new Ingredient in the database
     *
     * @param recipeId recipe identifier
     * @param name name of the ingredient
     * @param  measurement measurement
     * @throws NoSuchElementException if no Recipe found.
     */
    public void createNew(Long recipeId, String name, String measurement) throws NoSuchElementException {
        LOGGER.info("Create Ingredient for Recipe {} ", recipeId);
        ingredientRepository.save(new Ingredient(name, measurement, verifyRecipe(recipeId)));
    }

    /**
     * Get an ingredient by id.
     *
     * @param ingredientId ingredientId identifier
     * @return Ingredient
     */
    public Optional<Ingredient> lookupIngredientById(Long ingredientId)  {
        return ingredientRepository.findById(ingredientId);
    }

    /**
     * Get a page of recipe for an ingredient.
     *
     * @param recipeId recipe identifier
     * @param pageable page parameters to determine which elements to fetch
     * @return Page of Ingredients
     * @throws NoSuchElementException if no Ingredient found.
     */
    public Page<Ingredient> lookupIngredients(Long recipeId, Pageable pageable) throws NoSuchElementException  {
        LOGGER.info("Lookup Ingredient for recipe {}", recipeId);
        return ingredientRepository.findByRecipe(verifyRecipe(recipeId), pageable);
    }

    /**
     * Update the elements of an Ingredient.
     *
     * @param ingredientId recipe identifier
     * @param ingredientDto name of an ingredient
     * @return Ingredient Ingredient Domain Object
     * @throws NoSuchElementException if no Ingredient found.
     */
    public Ingredient update(Long ingredientId, IngredientDto ingredientDto) throws NoSuchElementException {
        LOGGER.info("Update ingredient {}", ingredientId);
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new NoSuchElementException("No such ingredient present " + ingredientId)));
        ingredient.get().setName(ingredientDto.getName());
        ingredient.get().setMeasurement(ingredientDto.getMeasurement());
        return ingredientRepository.save(ingredient.get());
    }

    /**
     * Delete an Ingredient.
     *
     * @param recipeId recipe identifier
     * @param ingredientId customer identifier
     * @throws NoSuchElementException if no Ingredient found.
     */
    public void delete(Long recipeId, Long ingredientId) throws NoSuchElementException {
        LOGGER.info("Delete Ingredient for recipe {} and customer {}", recipeId, ingredientId);
        Ingredient ingredient = verifyRecipeAndIngredient(recipeId, ingredientId);
        ingredientRepository.delete(ingredient);
    }

    /**
     * Delete an Ingredient.
     *
     * @param ingredientId customer identifier
     * @throws NoSuchElementException if no Ingredient found.
     */
    public void deleteById(Long ingredientId) throws NoSuchElementException {
        LOGGER.info("Delete Ingredient {}", ingredientId);
        Optional<Ingredient> ingredient = Optional.ofNullable(ingredientRepository.findById(ingredientId).orElseThrow(() ->
                new NoSuchElementException("No such ingredient present " + ingredientId)));
        ingredientRepository.delete(ingredient.get());
    }

    /**
     * Verify and return the Recipe given a recipeId.
     *
     * @param recipeId recipe id
     * @return the Recipe
     * @throws NoSuchElementException if no Recipe found.
     */
    public Recipe verifyRecipe(Long recipeId) throws NoSuchElementException {
        return recipeRepository.findById(recipeId).orElseThrow(() ->
                new NoSuchElementException("Recipe does not exist " + recipeId)
        );
    }


    /**
     * Verify and return the Ingredient for a particular recipeId and ingredientId
     * @param recipeId recipeId
     * @param ingredientId id
     * @return the found Ingredient
     * @throws NoSuchElementException if no Ingredient found
     */
    public Ingredient verifyRecipeAndIngredient(Long recipeId, Long ingredientId) throws NoSuchElementException {
        return ingredientRepository.findByRecipeAndIngredientId(verifyRecipe(recipeId), ingredientId).orElseThrow(() ->
                new NoSuchElementException("Recipe-Ingredient pair for request("
                        + recipeId + " for ingredient" + ingredientId));
    }
}
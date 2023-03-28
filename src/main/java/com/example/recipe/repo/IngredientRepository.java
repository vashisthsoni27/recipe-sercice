package com.example.recipe.repo;

import java.util.List;
import java.util.Optional;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Ingredient Repository Interface
 *
 * Created by Vashisth Soni
 */
@RepositoryRestResource(exported = false)
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Lookup all the Ingredients for a recipe.
     *
     * @param recipe recipe is the recipe Identifier
     * @return a List of any found Ingredients
     */
    List<Ingredient> findByRecipe(Recipe recipe);

    /**
     * Lookup a page of Ingredients for a recipe.
     *
     * @param recipe recipe is the recipe Identifier
     * @param pageable details for the desired page
     * @return a Page of any found Ingredients
     */
    Page<Ingredient> findByRecipe(Recipe recipe, Pageable pageable);

    /**
     * Lookup a Ingredient by the recipe and ingredient Id
     * @param recipe
     * @param id
     * @return Ingredients if found, null otherwise.
     */
    Optional<Ingredient> findByRecipeAndIngredientId(Recipe recipe, Long id);
}
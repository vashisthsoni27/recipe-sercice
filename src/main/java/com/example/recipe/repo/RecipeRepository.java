package com.example.recipe.repo;

import com.example.recipe.domain.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Recipe Repository Interface
 *
 * Created by Vashisth Soni
 */
public interface RecipeRepository extends PagingAndSortingRepository<Recipe,Long> {
    @Override
    @RestResource(exported = false)
    <S extends Recipe> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Recipe> Iterable<S> saveAll(Iterable<S> iterable);


    @Override
    @RestResource(exported = false)
    void deleteById(Long integer);


    @Override
    @RestResource(exported = false)
    void delete(Recipe recipe);


    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Recipe> iterable);


    @Override
    @RestResource(exported = false)
    void deleteAll();

    /**
     * Lookup a page of Recipes.
     *
     * @param recipeId recipeId is the recipe Identifier
     * @param pageable details for the desired page
     * @return a Page of any found recipe
     */
    Page<Recipe> findByRecipeId(Long recipeId, Pageable pageable);
}
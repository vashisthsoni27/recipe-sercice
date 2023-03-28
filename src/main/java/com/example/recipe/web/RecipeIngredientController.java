package com.example.recipe.web;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recipe ingredient Controller
 *
 * Created by Vashisth Soni
 */
@RestController
@RequestMapping(path = "/recipes/{recipeId}/ingredients")
public class RecipeIngredientController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeIngredientController.class);
    private final IngredientService ingredientService;
    private final IngredientAssembler assembler;


    @Autowired
    public RecipeIngredientController(IngredientService ingredientService,
                                      IngredientAssembler assembler) {
        this.ingredientService = ingredientService;
        this.assembler = assembler;
    }

    /**
     * Create an Ingredient.
     *
     * @param recipeId recipeId
     * @param ingredientDto ingredientDto
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CSR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createIngredient(@PathVariable(value = "recipeId") Long recipeId, @RequestBody @Validated IngredientDto ingredientDto) {
        LOGGER.info("POST /recipes/{}/ingredients", recipeId);
        ingredientService.createNew(recipeId, ingredientDto.getName(), ingredientDto.getMeasurement());
    }

    /**
     * Lookup a the ingredients for a recipe.
     *
     * @param recipeId recipeId
     * @param pageable pageable
     * @param pagedAssembler pagedAssembler
     * @return HATEOAS enabled page of ingredients.
     */
    @GetMapping
    public PagedResources<IngredientDto> getAllIngredientsForRecipe(@PathVariable(value = "recipeId") Long recipeId, Pageable pageable,
                                                                    PagedResourcesAssembler pagedAssembler) {
        LOGGER.info("GET /recipes/{}/ingredients", recipeId);
        Page<Ingredient> ingredientsPage = ingredientService.lookupIngredients(recipeId, pageable);
        return pagedAssembler.toResource(ingredientsPage, assembler);
    }

    /**
     * Delete an ingredient of a recipe
     *
     * @param recipeId recipeId
     * @param ingredientId ingredientId
     */
    @DeleteMapping("/{ingredientId}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public void delete(@PathVariable(value = "recipeId") Long recipeId, @PathVariable(value = "ingredientId") Long ingredientId) {
        LOGGER.info("DELETE /recipes/{}/ingredients/{}", recipeId, ingredientId);
        ingredientService.delete(recipeId, ingredientId);
    }
}
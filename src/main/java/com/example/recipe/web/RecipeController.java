package com.example.recipe.web;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.service.RecipeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recipe Controller
 *
 * Created by Vashisth Soni
 */
@RestController
@RequestMapping(path = "/recipes/")
public class RecipeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    private final RecipeAssembler assembler;

    private final EntityManager entityManager;

    public RecipeController(RecipeService recipeService, RecipeAssembler assembler, EntityManager entityManager) {
        this.recipeService = recipeService;
        this.assembler = assembler;
        this.entityManager = entityManager;
    }

    /**
     * Create a Recipe.
     *
     * @param recipeDto recepiDto
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CSR')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@RequestBody @Validated RecipeDto recipeDto) {
        LOGGER.info("POST /recipes/{} ", recipeDto.getName());
        recipeService.createNew(recipeDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find recipes by id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Recipe not found") })
    public RecipeDto getRecipe(
            @PathVariable("id") Long id) {
        LOGGER.info("GET /recipes/{} ", id);
        return assembler.toResource(recipeService.lookupRecipeById(id)
                .orElseThrow(() -> new NoSuchElementException("Recipe " + id + " not found"))
        );
    }

    /**
     * Delete a recipe by id.
     *
     * @param recipeId recipeId
     */
    @DeleteMapping("/{recipeId}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public void delete(@PathVariable(value = "recipeId") Long recipeId) {
        LOGGER.info("DELETE /recipes/{}", recipeId);
        recipeService.deleteById(recipeId);
    }

    /**
     * Update recipe by recipe id.
     *
     * @param recipeId recipeId
     * @param recipeDto recipeDto
     * @return The modified Recipe
     */
    @PutMapping("/{recipeId}")
    // @PreAuthorize("hasRole('ROLE_CSR')")
    public RecipeDto updateWithPut(@PathVariable(value = "recipeId") Long recipeId, @RequestBody @Validated RecipeDto recipeDto) {
        LOGGER.info("PUT /recipe/{}", recipeId);
        return toDto(recipeService.update(recipeId, recipeDto));
    }

    @PostMapping("/search")
    public List<Recipe> searchRecipes(@RequestBody RecipeSearchCriteria searchCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> query = builder.createQuery(Recipe.class);
        Root<Recipe> root = query.from(Recipe.class);

        List<Predicate> predicates = new ArrayList<>();

        if (searchCriteria.isVegetarian()) {
            predicates.add(builder.equal(root.get("isVegan"), true));
        }

        if (searchCriteria.getServes() != null) {
            predicates.add(builder.equal(root.get("noOfServings"), searchCriteria.getServes()));
        }

        if (searchCriteria.getIngredientName() != null) {
            Join<Recipe, Ingredient> ingredientJoin = root.join("ingredients");
            predicates.add(builder.equal(ingredientJoin.get("name"), searchCriteria.getIngredientName()));
        }

        if (searchCriteria.getExcludeIngredients() != null) {
            Join<Recipe, Ingredient> ingredientJoin = root.join("ingredients");
            predicates.add(builder.notEqual(ingredientJoin.get("name"), searchCriteria.getExcludeIngredients()));
        }

        if (searchCriteria.getIncludeInstructions() != null) {
            predicates.add(builder.like(root.get("instructions"), "%" + searchCriteria.getIncludeInstructions() + "%"));
        }

        query.where(builder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Convert the recipe entity to a recipeDto
     *
     * @param recipe recipe
     * @return recipeDto
     */
    private RecipeDto toDto(Recipe recipe) {
        return assembler.toResource(recipe);
    }

}
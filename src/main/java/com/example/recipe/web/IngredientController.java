package com.example.recipe.web;

import java.util.NoSuchElementException;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.service.IngredientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ingredient Controller
 *
 * Created by Vashisth Soni
 */
@RestController
@RequestMapping(path = "/ingredients/")
public class IngredientController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientController.class);
    private final IngredientService ingredientService;
    private final IngredientAssembler assembler;


    @Autowired
    public IngredientController(IngredientService ingredientService,
                                IngredientAssembler assembler) {
        this.ingredientService = ingredientService;
        this.assembler = assembler;
    }

    /**
     * Update name and measurement of an ingredient
     *
     * @param ingredientId ingredientId
     * @param ingredientDto ingredientDto
     * @return The modified Ingredient Dto
     */
    @PutMapping("/{ingredientId}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public IngredientDto updateWithPut(@PathVariable(value = "ingredientId") Long ingredientId, @RequestBody @Validated IngredientDto ingredientDto) {
        LOGGER.info("PUT ingredients/{}", ingredientId);
        return toDto(ingredientService.update(ingredientId, ingredientDto));
    }

    /**
     * Delete an ingredient by Id
     *
     * @param ingredientId ingredientId
     */
    @DeleteMapping("/{ingredientId}")
    @PreAuthorize("hasRole('ROLE_CSR')")
    public void delete(@PathVariable(value = "ingredientId") Long ingredientId) {
        LOGGER.info("DELETE /ingredients/{}", ingredientId);
        ingredientService.deleteById(ingredientId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find ingredient by id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Ingredient not found") })
    public IngredientDto getIngredient(
            @PathVariable("id") Long id) {
        LOGGER.info("GET /ingredients/{} ", id);
        return assembler.toResource(ingredientService.lookupIngredientById(id)
                .orElseThrow(() -> new NoSuchElementException("Ingredient " + id + " not found"))
        );
    }

    /**
     * Convert the ingredient entity to a IngredientDto
     *
     * @param ingredient ingredient
     * @return IngredientDto IngredientDto
     */
    private IngredientDto toDto(Ingredient ingredient) {
        return assembler.toResource(ingredient);
    }

}
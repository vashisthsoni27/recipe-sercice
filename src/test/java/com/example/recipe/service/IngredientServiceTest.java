package com.example.recipe.service;

import java.util.Collections;
import java.util.Optional;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.repo.IngredientRepository;
import com.example.recipe.repo.RecipeRepository;
import com.example.recipe.web.IngredientDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vashisth Soni
 */
@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceTest {

    private static final long INGREDIENT_ID = 1L;

    private static final long RECIPE_ID = 1L;

    @Mock
    private IngredientRepository ingredientRepositoryMock;

    @Mock
    private RecipeRepository recipeRepositoryMock;

    @InjectMocks
    private IngredientService ingredientServiceMock;

    @Mock
    private Ingredient ingredientMock;

    @Mock
    private Recipe recipeMock;


    /**
     * Mock responses to commonly invoked methods.
     */
    @Before
    public void setupReturnValuesOfMockMethods() {
        when(ingredientRepositoryMock.findById(INGREDIENT_ID)).thenReturn(Optional.of(ingredientMock));
        when(recipeRepositoryMock.findById(RECIPE_ID)).thenReturn(Optional.of(recipeMock));
    }

    /******************************
     *
     * Verify the service return value
     *
     ******************************/
    @Test
    public void lookupRecipeById() {
        when(ingredientServiceMock.lookupIngredientById(INGREDIENT_ID)).thenReturn(Optional.of(ingredientMock));

        //invoke and verify lookupIngredientById
        assertThat(ingredientServiceMock.lookupIngredientById(INGREDIENT_ID).get(), is(ingredientMock));
    }

    @Test
    public void lookupAll() {
        when(ingredientRepositoryMock.findAll()).thenReturn(Collections.singletonList(ingredientMock));

        //invoke and verify lookupAll
        assertThat(ingredientRepositoryMock.findAll().iterator().next(), is(ingredientMock));
    }

    /******************************
     *
     * Verify the invocation of dependencies.
     *
     ******************************/

    @Test
    public void delete() {
        //invoke delete
        ingredientServiceMock.deleteById(1L);

        //verify ingredientRepository.delete invoked
        verify(ingredientRepositoryMock).delete(any(Ingredient.class));
    }

    @Test
    public void update() {
        //invoke update
        ingredientServiceMock.update(INGREDIENT_ID,new IngredientDto("test", "test"));

        //verify ingredientRepository.save invoked once
        verify(ingredientRepositoryMock).save(any(Ingredient.class));

        //verify and ingredientMock setter methods invoked
        verify(ingredientMock).setName("test");
        verify(ingredientMock).setMeasurement("test");
    }

    /******************************
     *
     * Verify the invocation of dependencies
     * Capture parameter values.
     * Verify the parameters.
     *
     ******************************/

    @Test
    public void createNew() {
        //prepare to capture a Ingredient Object
        ArgumentCaptor<Ingredient> ingredientArgumentCaptor = ArgumentCaptor.forClass(Ingredient.class);
        when(ingredientServiceMock.lookupIngredientById(INGREDIENT_ID)).thenReturn(Optional.of(ingredientMock));
        //invoke createNew
        ingredientServiceMock.createNew(RECIPE_ID, "test", "test");

        //verify ingredientRepository.save invoked once and capture the Ingredient Object
        verify(ingredientRepositoryMock).save(ingredientArgumentCaptor.capture());

        //verify the attributes of the Ingredient Object
        assertThat(ingredientArgumentCaptor.getValue().getName(), is("test"));
        assertThat(ingredientArgumentCaptor.getValue().getMeasurement(), is("test"));
    }
}
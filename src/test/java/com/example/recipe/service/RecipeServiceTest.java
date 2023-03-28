package com.example.recipe.service;

import java.util.Arrays;
import java.util.Optional;

import com.example.recipe.domain.Recipe;
import com.example.recipe.repo.RecipeRepository;
import com.example.recipe.web.RecipeDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vashisth Soni
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    private static final long RECIPE_ID = 1L;

    @Mock
    private RecipeRepository recipeRepositoryMock;

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private Recipe recipeMock;


    /**
     * Mock responses to commonly invoked methods.
     */
    @Before
    public void setupReturnValuesOfMockMethods() {
        when(recipeRepositoryMock.findById(RECIPE_ID)).thenReturn(Optional.of(recipeMock));
        when(recipeMock.getRecipeId()).thenReturn(RECIPE_ID);
    }

    /******************************
     *
     * Verify the service return value
     *
     ******************************/
    @Test
    public void lookupRecipeById() {
        when(recipeService.lookupRecipeById(RECIPE_ID)).thenReturn(Optional.of(recipeMock));

        //invoke and verify lookupRecipeById
        assertThat(recipeService.lookupRecipeById(RECIPE_ID).get(), is(recipeMock));
    }

    @Test
    public void lookupAll() {
        when(recipeRepositoryMock.findAll()).thenReturn(Arrays.asList(recipeMock));

        //invoke and verify lookupAll
        assertThat(recipeRepositoryMock.findAll().iterator().next(), is(recipeMock));
    }

    @Test
    public void lookupRecipes() {
        //create mocks of Pageable and Page (only needed in this test)
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);
        when(recipeRepositoryMock.findByRecipeId(RECIPE_ID, pageable)).thenReturn(page);

        //invoke and verify lookupRecipe
        assertThat(recipeService.lookupRecipe(RECIPE_ID, pageable), is(page));
    }

    /******************************
     *
     * Verify the invocation of dependencies.
     *
     ******************************/

    @Test
    public void delete() {
        //invoke delete
        recipeService.deleteById(1L);

        //verify recipeRepository.delete invoked
        verify(recipeRepositoryMock).delete(any(Recipe.class));
    }

    @Test
    public void update() {
        //invoke update
        recipeService.update(RECIPE_ID,new RecipeDto("test", "test", false, true, 5));

        //verify recipeRepositoryMock.save invoked once
        verify(recipeRepositoryMock).save(any(Recipe.class));

        //verify and recipe setter methods invoked
        verify(recipeMock).setName("test");
        verify(recipeMock).setNoOfServings(5);
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
        //prepare to capture a Recipe Object
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //invoke createNew
        recipeService.createNew(new RecipeDto("test", "test", false, true, 5));

        //verify recipeRepository.save invoked once and capture the Recipe Object
        verify(recipeRepositoryMock).save(recipeArgumentCaptor.capture());

        //verify the attributes of the Recipe Object
        assertThat(recipeArgumentCaptor.getValue().getNoOfServings(), is(5));
        assertThat(recipeArgumentCaptor.getValue().getName(), is("test"));
    }
}
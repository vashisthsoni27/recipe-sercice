package com.example.recipe.web;

import java.util.Collections;
import java.util.List;

import com.example.recipe.domain.Ingredient;
import com.example.recipe.domain.Recipe;
import com.example.recipe.service.IngredientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

/**
 *
 * Invoke the Controller methods via HTTP.
 *
 * Created by Vashisth Soni.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class RecipeIngredientControllerTest {

    private static final long RECIPE_ID = 1L;
    private static final long INGREDIENT_ID = 1L;
    private static final String NAME = "TEST";
    private static final String MEASUREMENT = "measurement";
    private static final String RECIPE_INGREDIENTS_URL = "/recipes/" + RECIPE_ID + "/ingredients";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtRequestHelper jwtRequestHelper;


    @MockBean
    private IngredientService serviceMock;

    @Mock
    private Ingredient ingredientMock;

    @Mock
    private Recipe recipeMock;

    private final IngredientDto ingredientDto = new IngredientDto(NAME, MEASUREMENT);

    @Before
    public void setupReturnValuesOfMockMethods() {
        when(ingredientMock.getName()).thenReturn(NAME);
        when(ingredientMock.getMeasurement()).thenReturn(MEASUREMENT);
        when(ingredientMock.getIngredientId()).thenReturn(INGREDIENT_ID);
        when(ingredientMock.getRecipe()).thenReturn(recipeMock);
        when(recipeMock.getRecipeId()).thenReturn(RECIPE_ID);
    }

    /**
     *  HTTP POST /recipes/{recipeId}/ingredients
     */
    @Test
    public void createRecipeIngredient() {

        restTemplate.exchange(RECIPE_INGREDIENTS_URL, POST,
                new HttpEntity(ingredientDto, jwtRequestHelper.withRole("ROLE_CSR")),
                Void.class);

        verify(this.serviceMock).createNew(RECIPE_ID, NAME, MEASUREMENT);
    }

    /**
     *  HTTP DELETE /recipes/{recipeId}/ingredients
     */
    @Test
    public void delete() {

        restTemplate.exchange(RECIPE_INGREDIENTS_URL + "/" + INGREDIENT_ID, DELETE,
                new HttpEntity(jwtRequestHelper.withRole("ROLE_CSR")), Void.class);

        verify(serviceMock).delete(RECIPE_ID, INGREDIENT_ID);
    }

    /**
     *  HTTP GET /recipes/{recipeId}/ingredients
     */
    @Test
    public void getAllIngredientsForRecipe() {
        List<Ingredient> listOfIngredients = Collections.singletonList(ingredientMock);
        PageImpl page = new PageImpl(listOfIngredients, PageRequest.of(0,10),1);
        when(serviceMock.lookupIngredients(anyLong(),any(Pageable.class))).thenReturn(page);

        ResponseEntity<String> response = restTemplate.getForEntity(RECIPE_INGREDIENTS_URL,String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        verify(serviceMock).lookupIngredients(anyLong(), any(Pageable.class));
    }
}
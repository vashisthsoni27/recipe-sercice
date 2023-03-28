CREATE TABLE ingredient (
                             ingredient_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             recipe_id BIGINT,
                             name VARCHAR(255),
                             measurement VARCHAR(255));

ALTER TABLE ingredient ADD CONSTRAINT FK_recipe_id FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id);
ALTER TABLE ingredient ADD UNIQUE IngredientConstraint (ingredient_id, recipe_id);
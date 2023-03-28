
CREATE TABLE recipe (
  recipe_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  instructions VARCHAR(2000) NOT NULL,
  contains_meat boolean NOT NULL,
  is_vegan boolean NOT NULL,
  no_of_servings NUMERIC NOT NULL
);
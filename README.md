
# Recipe Management Microservice

#### Setup
Set JAVA_HOME

Set M2_HOME

Add M2_HOME/bin to the execution path
#### Docker Commands
##### Start MySql Container (downloads image if not found)
``
docker run  --detach   --name ec-mysql -p 6604:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=recipe -e MYSQL_USER=recipe_user -e MYSQL_PASSWORD=recipe_pass -d mysql
``

##### Technologies Used
``
Java 11
``
``
Spring Boot 2.0.1
``
``
MySql
``
``
Docker
``
``
Postman
``
``
Flyway
``
#### Steps to start application
1 Import the project from Bitbucket.

2 Start RecipeServiceApplication.java

3 Open http://localhost:8080/swagger-ui.html for all the API information.

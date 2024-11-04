# Blackjack API

This is an API developed in Java with Spring Boot for a Blackjack game. The API allows for managing players, games, and the rules of the game, utilizing two databases: MongoDB and MySQL. It includes functionalities to play, obtain rankings and much more.

## Features

- Reactive implementation with Spring WebFlux.
- Management of players and games with persistence in MongoDB and MySQL.
- Global exception handling.
- Automatic documentation with Swagger.
- Unit testing with JUnit and Mockito.

## Requirements

- Java 11 or higher
- Maven
- MongoDB
- MySQL

## Installation

1. Clone this repository:

git clone https://github.com/clareta16/Blackjack.git

2. Navigate to the project directory:

cd cat.itacademy.s05.t01.n01.blackjack

3. Install dependencies using Maven:

mvn clean install

## Execution

4. Configure the application:
   - Update your MongoDB connection details in application.properties under the src/main/resources directory.
   - Set up your MySQL connection details in the same file.
     Execution

1. Make sure MongoDB and MySQL are running on your local machine or on a configured server.

2. Start the application:

mvn spring-boot:run

3.  The application will be available at http://localhost:8080
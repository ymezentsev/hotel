# Hotel room accounting system

Spring Web Application based on Spring Boot and Java.

The application is intended for the administration of work with information about hotel rooms. The user can add new
numbers, delete/update information about current ones; store information about the visitor, search for the visitor by
last name/passport number, can update information about the visitor; find available rooms on a given date, book a room.

The file "database_structure.svg" represents the structure of the database.

To view and test the application in Postman, you need to import the collection of requests from the "hotel.postman_collection.json" file.

Generated OpenApi documentation avaliable (when app running) [Swagger-ui](http://localhost:8080/swagger-ui/index.html)
and [OAS](http://localhost:8080/v3/api-docs)

## Getting Started

To run this project, you need to have the following software installed on your computer:

- JDK 17
- Docker

### Running the Project

Follow these steps to get your application up and running:

1. Ensure Docker is running.

2. Run the Hotel application.

## Tech stack

- Spring Boot
- Spring Data
- MySQL
- FlyWay
- Swagger/OpenApi 3.0
- JUnit 5
- Docker
- Docker-compose
- Testcontainers

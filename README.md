Hotel room accounting system.

Spring Web Application based on Spring Boot and Java.

The application is intended for the administration of work with information about hotel rooms. The user can add new numbers, delete/update information about current ones; store information about the visitor, search for the visitor by last name/passport number, can update information about the visitor; find available rooms on a given date, book a room.

The application works with the H2 database, the test database is "hotel.mv.db". For the launch the database and correct work of the application at first at the project settings, you must select the "application-h2.yaml" configuration file.

The file "database_structure.svg" represents the structure of the database.

To view and test the application in Postman, you need to import the collection of requests from the "hotel.postman_collection.json" file.

The ability to test with swagger is also available at "http://localhost:8080/swagger-ui/index.html".

FROM openjdk:17
COPY target/hotel-2.0.1-SNAPSHOT.jar hotel.jar
EXPOSE 8080

CMD ["java", "-jar", "hotel.jar"]
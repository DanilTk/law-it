FROM amazoncorretto:21-alpine

WORKDIR /app

COPY application/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
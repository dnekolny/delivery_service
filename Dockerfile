FROM openjdk:8-jdk-alpine

COPY target/delivery_service-0.0.1-SNAPSHOT.jar /app/delivery_service.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/delivery_service.jar"]
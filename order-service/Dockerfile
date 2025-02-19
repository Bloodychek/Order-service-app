FROM openjdk:17-jdk-slim
LABEL authors="Bloodychek"
WORKDIR /app

COPY target/order-service-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
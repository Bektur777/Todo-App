FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/admin-service-0.0.1-SNAPSHOT.jar /app/admin-service.jar

EXPOSE 8083

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/admin-service.jar"]

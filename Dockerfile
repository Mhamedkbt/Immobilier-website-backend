# Stage 1: Build
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/*.jar app.jar

# Copy .env file
COPY .env .env

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

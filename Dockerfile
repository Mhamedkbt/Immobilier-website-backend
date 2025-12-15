# Stage 1: Build the application using Maven + JDK 21
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the Spring Boot application without running tests
RUN mvn clean package -DskipTests

# Stage 2: Run the application using JDK 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

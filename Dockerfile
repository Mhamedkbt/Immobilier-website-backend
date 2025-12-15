# Use JDK 21 to run the backend
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copy the JAR you built locally
COPY target/Standard-ecommerce-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose backend port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copy the source code
COPY src src

# Build the application
RUN mvn clean install -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/MT-*.jar app.jar

# Expose the application port
EXPOSE 3839

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=default

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:3839/actuator/health || exit 1

# Entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS builder


WORKDIR /app

# Copy pom.xml first to leverage caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080
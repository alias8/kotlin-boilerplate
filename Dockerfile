# Build stage
FROM gradle:8.12-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]

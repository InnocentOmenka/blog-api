# Use the official maven image to create a build artifact.
# This is the first stage of a multi-stage build.
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use the official OpenJDK image for a lean production stage.
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the jar file from the previous stage
COPY --from=build /app/target/blog-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]


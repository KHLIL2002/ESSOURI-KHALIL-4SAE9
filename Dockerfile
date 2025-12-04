# Use a base image with JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Install netcat (nc) utility
RUN apt-get update && apt-get install -y netcat
# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from Maven build output to the container
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8082 (configured in Spring Boot)
EXPOSE 8089

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Use an official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your host to the container
COPY target/mall-admin-1.0-SNAPSHOT.jar /app/mall-admin.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/mall-admin.jar"]
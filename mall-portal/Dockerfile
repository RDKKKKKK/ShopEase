FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/mall-portal-1.0-SNAPSHOT.jar /app/mall-portal.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/mall-portal.jar"]
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/mall-search-1.0-SNAPSHOT.jar /app/mall-search.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/mall-search.jar"]
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/mall-auth-1.0-SNAPSHOT.jar /app/mall-auth.jar
EXPOSE 8401
ENTRYPOINT ["java", "-jar", "/app/mall-auth.jar"]
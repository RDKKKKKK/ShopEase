FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/mall-gateway-1.0-SNAPSHOT.jar /app/mall-gateway.jar
EXPOSE 8201
ENTRYPOINT ["java", "-jar", "/app/mall-gateway.jar"]
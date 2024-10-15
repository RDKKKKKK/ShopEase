FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/mall-monitor-1.0-SNAPSHOT.jar /app/mall-monitor.jar
EXPOSE 8101
ENTRYPOINT ["java", "-jar", "/app/mall-monitor.jar"]
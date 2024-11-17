FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

COPY  /shopease-mall-cloud-recommend-web/target/*.jar /app/recommend.jar

# 暴露端口（根据需求修改）
EXPOSE 29020

# 启动脚本同时启动两个模块
ENTRYPOINT ["java", "-jar", "/app/recommend.jar"]
FROM openjdk:8-jdk-slim

# 设置工作目录
WORKDIR /app

COPY  /shopease-mall-cloud-order-web/target/*.jar /app/order.jar

# 暴露端口（根据需求修改）
EXPOSE 29040

# 启动脚本同时启动两个模块
ENTRYPOINT ["java", "-jar", "/app/order.jar"]
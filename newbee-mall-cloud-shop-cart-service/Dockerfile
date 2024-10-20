FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

COPY  /newbee-mall-cloud-shop-cart-web/target/*.jar /app/shop-cart.jar

# 暴露端口（根据需求修改）
EXPOSE 29030

# 启动脚本同时启动两个模块
ENTRYPOINT ["java", "-jar", "/app/shop-cart.jar"]
# 第一阶段：构建前端应用
FROM node:14 AS build

# 设置工作目录
WORKDIR /app

# 复制依赖文件
COPY package*.json ./

# 安装依赖
RUN npm install

# 复制其余的应用代码并构建
COPY . .
RUN npm run build

# 第二阶段：使用 Nginx 提供服务
FROM nginx:alpine

# 复制 Nginx 配置文件到镜像中
COPY nginx.conf /etc/nginx/nginx.conf

# 复制构建后的文件到 Nginx 的文档根目录
COPY --from=build /app/dist /usr/share/nginx/html

# 暴露 Nginx 的 80 端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
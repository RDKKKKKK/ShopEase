<template>
  <div class="intro-page">
    <div class="intro-container">
      <h1 class="title">Welcome to ShopEase Backend Management System</h1>
      <p class="description">
        This is a comprehensive backend management platform designed to
        manage products, users, orders, and more. It provides powerful tools
        to streamline business operations and enhance efficiency.
      </p>
      <ul class="features">
        <li>📦 Product Management</li>
        <li>👥 User Management</li>
        <li>🛒 Order Management</li>
<!--        <li>📊 Data Analytics and Reports</li>-->
        <li>⚙️ System Settings and Configurations</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
// 这里可以加一些逻辑代码，如果有需要的功能组件
</script>

<style scoped>
.intro-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  padding: 20px;
}

.intro-container {
  text-align: center;
  background-color: #fff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  width: 100%;
}

.title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.description {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
  line-height: 1.6;
}

.features {
  list-style: none;
  padding: 0;
}

.features li {
  font-size: 16px;
  margin: 10px 0;
  color: #444;
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>

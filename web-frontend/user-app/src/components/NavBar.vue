
<template>
  <div class="nav-bar van-hairline--top">
    <ul class="nav-list">
      <router-link  class="nav-list-item active" to="home">
        <i class="nbicon nblvsefenkaicankaoxianban-1"></i>
        <span>Home</span>
      </router-link>
      <router-link  class="nav-list-item" to="category">
        <i class="nbicon nbfenlei"></i>
        <span>Classification</span>
      </router-link>
      <router-link  class="nav-list-item" to="cart">
        <i><van-icon  name="shopping-cart-o" :badge="!cart.count ? '' : cart.count" /></i>
        <span>Cart</span>
      </router-link>
      <router-link  class="nav-list-item" to="user">
        <i class="nbicon nblvsefenkaicankaoxianban-"></i>
        <span>Person</span>
      </router-link>
    </ul>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { getLocal } from '@/common/js/utils'
const route = useRoute()
const cart = useCartStore()
onMounted(() => {
  const token = getLocal('token')
  const path = route.path
  if (token && !['/home', '/category'].includes(path)) {
    cart.updateCart()
  }
})
</script>

<style lang="less" scoped>
@import '../common/style/mixin';
@primary: #FFA500; // 橙色
@secondary: #FFD700; // 黄色

.nav-bar {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  padding: 1px 0; // 增加顶部和底部内边距
  z-index: 1000;
  background: @primary; // 背景改为橙色
  border-top: 0px solid @secondary; // 添加顶部边框
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1); // 添加阴影效果
  transform: translateZ(0);
  -webkit-transform: translateZ(0);

  .nav-list {
    width: 100%;
    display: flex; // 确保是 flex 布局
    justify-content: space-around; // 平均分配项目
    padding: 0;

    .nav-list-item {
      display: flex;
      flex-direction: column;
      align-items: center; // 使内容居中
      color: #fff; // 字体颜色改为白色以提高对比度
      transition: color 0.3s; // 添加过渡效果

      &.router-link-active {
        color: @secondary; // 激活状态字体颜色
      }

      i {
        text-align: center;
        font-size: 25px; // 增加图标大小
        margin-bottom: 1px; // 增加图标与文本的间距
      }

      span {
        font-size: 14px; // 增加文本字体大小
      }

      .van-icon-shopping-cart-o {
        margin: 0 auto;
        margin-bottom: 0px; // 增加间距
      }

      // 添加悬停效果
      &:hover {
        color: @secondary; // 悬停时改变颜色
      }
    }
  }
}
</style>


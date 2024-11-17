<template>
  <div>
    <header class="home-header wrap" :class="{ 'active': state.headerScroll }">
      <router-link tag="i" to="./category"><i class="nbicon nbmenu2"></i></router-link>
      <div class="header-search">
        <span class="app-name"></span>
        <i class="iconfont icon-search"></i>
        <router-link tag="span" class="search-title" to="./product-list?from=home">Search</router-link>
      </div>
      <router-link class="login" tag="span" to="./login" v-if="!state.isLogin">Login</router-link>
      <router-link class="login" tag="span" to="./user" v-else>
        <van-icon name="manager-o" />
      </router-link>
    </header>
    <nav-bar />
    <swiper :list="state.swiperList"></swiper>
    <div class="category-list">
      <div v-for="item in state.categoryList" v-bind:key="item.categoryId" @click="tips">
        <img :src="item.imgUrl">
        <span>{{ item.name }}</span>
      </div>
    </div>
    <div class="good">
      <header class="good-header">New Items</header>
      <van-skeleton title :row="3" :loading="state.loading">
        <div class="good-box">
          <div class="good-item" v-for="item in state.newGoodses" :key="item.goodsId" @click="goToDetail(item)">
            <img :src="$filters.prefix(item.goodsCoverImg)" alt="">
            <div class="good-desc">
              <div class="title">{{ item.goodsName }}</div>
              <div class="price">¥ {{ item.sellingPrice }}</div>
            </div>
          </div>
        </div>
      </van-skeleton>
    </div>
    <div class="good">
      <header class="good-header">Popular</header>
      <van-skeleton title :row="3" :loading="state.loading">
        <div class="good-box">
          <div class="good-item" v-for="item in state.hots" :key="item.goodsId" @click="goToDetail(item)">
            <img :src="$filters.prefix(item.goodsCoverImg)" alt="">
            <div class="good-desc">
              <div class="title">{{ item.goodsName }}</div>
              <div class="price">¥ {{ item.sellingPrice }}</div>
            </div>
          </div>
        </div>
      </van-skeleton>
    </div>
    <div class="good" :style="{ paddingBottom: '100px' }">
      <header class="good-header">Latest Recommendations</header>
      <van-skeleton title :row="3" :loading="state.loading">
        <div class="good-box">
          <div class="good-item" v-for="item in state.recommends" :key="item.goodsId" @click="goToDetail(item)">
            <img :src="$filters.prefix(item.goodsCoverImg)" alt="">
            <div class="good-desc">
              <div class="title">{{ item.goodsName }}</div>
              <div class="price">¥ {{ item.sellingPrice }}</div>
            </div>
          </div>
        </div>
      </van-skeleton>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import swiper from '@/components/Swiper.vue'
import navBar from '@/components/NavBar.vue'
import { getHome } from '@/service/home'
import { getLocal } from '@/common/js/utils'
import { showLoadingToast, closeToast, showToast } from 'vant'
import { useCartStore } from '@/stores/cart'
const cart = useCartStore()
const router = useRouter()
const state = reactive({
  swiperList: [], // 轮播图列表
  isLogin: false, // 是否已登录
  headerScroll: false, // 滚动透明判断
  hots: [],
  newGoodses: [],
  recommends: [],
  categoryList: [
  ],
  loading: true
})
onMounted(async () => {
  const token = getLocal('token')
  if (token) {
    state.isLogin = true
    // 获取购物车数据.
    cart.updateCart()
  }
  showLoadingToast({
    message: 'Loading...',
    forbidClick: true
  });
  const { data } = await getHome()
  state.swiperList = data.carousels
  state.newGoodses = data.newGoodses
  state.hots = data.hotGoodses
  state.recommends = data.recommendGoodses
  state.loading = false
  closeToast()
})

nextTick(() => {
  document.body.addEventListener('scroll', () => {
    let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
    scrollTop > 100 ? state.headerScroll = true : state.headerScroll = false
  })
})

const goToDetail = (item) => {
  router.push({ path: `/product/${item.goodsId}` })
}

const tips = () => {
  showToast('敬请期待');
}
</script>

<style lang="less" scoped>
@import '../common/style/mixin';

// 定义新颜色变量
@primary: #FF8C00; // 深橙色
@secondary: #FFD700; // 金色
@accent: #FF4500; // 橙红色
@background: #FFF8E7; // 浅杏色
@textColor: #4B2C1D; // 深咖啡色

.home-header {
  position: fixed;
  left: 0;
  top: 0;
  .wh(100%, 60px); // 调整高度
  .fj();
  line-height: 60px; // 与高度一致
  padding: 0 20px; // 调整内边距
  .boxSizing();
  font-size: 16px; // 增加字体大小
  color: #fff; // 保持文字颜色为白色以确保可读性
  z-index: 10000;
  background: @primary; // 头部背景使用深橙色

  .nbmenu2 {
    color: @secondary; // 使用金色
  }

  &.active {
    background: @accent; // 橙红色背景

    .nbmenu2 {
      color: #fff; // 保持为白色
    }

    .login {
      color: #fff; // 保持为白色
    }
  }

  .header-search {
    display: flex;
    width: 70%; // 调整宽度
    line-height: 22px; // 增加行高
    margin: 15px 0; // 调整外边距
    padding: 8px 10px; // 调整内边距
    color: @textColor; // 深咖啡色
    background: rgba(255, 225, 200, .9); // 更加柔和的浅橙色背景
    border-radius: 25px; // 增加圆角
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果

    .app-name {
      padding: 0 15px; // 调整内边距
      color: @secondary; // 金色
      font-size: 22px; // 增加字体大小
      font-weight: bold;
      border-right: 1px solid #FFDAB9; // 使用更浅的色调
    }

    .icon-search {
      padding: 0 10px;
      font-size: 20px; // 增加图标大小
    }

    .search-title {
      margin-left: -50px;
      font-size: 14px; // 增加字体大小
      color: #666; // 可根据需要调整
      line-height: 14px; // 增加行高
    }
  }

  .icon-iconyonghu {
    color: #fff; // 保持为白色
    font-size: 24px; // 增加图标大小
  }

  .login {
    color: @secondary; // 使用金色
    line-height: 60px; // 与头部高度一致

    .van-icon-manager-o {
      font-size: 22px; // 增加图标大小
      vertical-align: -3px;
    }
  }
}

.category-list {
  display: flex;
  flex-shrink: 0;
  flex-wrap: wrap;
  width: 100%;
  padding-bottom: 20px; // 增加底部内边距

  div {
    display: flex;
    flex-direction: column;
    width: 22%; // 增加宽度以便展示更多类别
    text-align: center;

    img {
      .wh(40px, 40px); // 增加图标大小
      margin: 15px auto 10px auto; // 调整间距
    }
  }
}

.good {
  .good-header {
    background: #FFE5B4; // 更加明亮的浅橙色
    height: 60px; // 增加高度
    line-height: 60px; // 与高度一致
    text-align: center;
    color: @primary; // 深橙色
    font-size: 18px; // 增加字体大小
    font-weight: 600; // 加粗字体
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); // 增加阴影效果
  }

  .good-box {
    display: flex;
    justify-content: space-between; // 确保商品均匀分布
    flex-wrap: wrap; // 允许换行
    padding: 0 10px; // 在商品容器中增加左右内边距

    .good-item {
      box-sizing: border-box;
      width: 48%; // 调整宽度以便两个商品在一行中展示
      border: 1px solid #FFDAB9; // 使用金色边框
      border-radius: 10px; // 增加圆角
      padding: 15px; // 增加内边距
      margin: 10px 0; // 添加外边距，顶部和底部
      background: #fff; // 白色背景
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); // 增加阴影效果

      img {
        display: block;
        width: 130px; // 调整图像宽度
        margin: 0 auto;
        border-radius: 10px; // 圆角效果
      }

      .good-desc {
        text-align: center;
        font-size: 16px; // 增加字体大小
        padding: 10px 0;

        .title {
          color: @textColor; // 深咖啡色
          font-weight: bold; // 加粗字体
        }

        .price {
          color: @primary; // 深橙色
          font-size: 18px; // 增加字体大小
        }
      }
    }
  }
}

.floor-list {
  width: 100%;
  padding-bottom: 60px; // 增加底部内边距

  .floor-head {
    width: 100%;
    height: 50px; // 增加高度
    background: #FFFAF0; // 非常浅的橙色
    text-align: center;
    line-height: 50px; // 与高度一致
    font-size: 18px; // 增加字体大小
    color: @textColor; // 深咖啡色
  }

  .floor-content {
    display: flex;
    flex-shrink: 0;
    flex-wrap: wrap;
    width: 100%;
    .boxSizing();

    .floor-category {
      width: 50%;
      padding: 15px; // 增加内边距
      border-right: 2px solid #FFDAB9; // 加粗边框
      border-bottom: 2px solid #FFDAB9; // 加粗边框
      .boxSizing();

      &:nth-child(2n) {
        border-right: none;
      }

      p {
        font-size: 18px; // 增加字体大小
        color: @textColor; // 深咖啡色

        &:nth-child(2) {
          padding: 7px 0; // 增加内边距
          font-size: 14px; // 增加字体大小
          color: @primary; // 深橙色
        }
      }

      .floor-products {
        .fj();
        width: 100%;

        img {
          .wh(75px, 75px); // 增加图像大小
        }
      }
    }
  }
}
</style>

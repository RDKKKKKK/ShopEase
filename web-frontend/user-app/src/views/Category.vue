
<template>
  <div class="categray">
    <div>
      <header class="category-header wrap van-hairline--bottom">
        <i class="nbicon nbfanhui" @click="goHome"></i>
        <div class="header-search">
          <i class="nbicon nbSearch"></i>
          <router-link tag="span" class="search-title" to="./product-list?from=category">Search</router-link>
        </div>
        <i class="iconfont icon-More"></i>
      </header>
      <nav-bar></nav-bar>
      <div class="search-wrap" ref="searchWrap">
        <list-scroll :scroll-data="state.categoryData" class="nav-side-wrapper">
          <ul class="nav-side">
            <li
              v-for="item in state.categoryData"
              :key="item.categoryId"
              v-text="item.categoryName"
              :class="{'active' : state.currentIndex == item.categoryId}"
              @click="selectMenu(item.categoryId)"
            ></li>
          </ul>
        </list-scroll>
        <div class="search-content">
          <list-scroll :scroll-data="state.categoryData" >
            <div class="swiper-container">
              <div class="swiper-wrapper">
                <template v-for="(category, index) in state.categoryData">
                  <div class="swiper-slide" v-if="state.currentIndex == category.categoryId" :key="index">
                    <!-- <img class="category-main-img" :src="category.mainImgUrl" v-if="category.mainImgUrl"/> -->
                    <div class="category-list" v-for="(products, index) in category.secondLevelCategoryVOS" :key="index">
                      <p class="catogory-title">{{products.categoryName}}</p>
                      <div class="product-item" v-for="(product, index) in products.thirdLevelCategoryVOS" :key="index" @click="selectProduct(product)">
                        <img src="//s.weituibao.com/1583591077131/%E5%88%86%E7%B1%BB.png" class="product-img"/>
                        <p v-text="product.categoryName" class="product-title"></p>
                      </div>
                    </div>
                  </div>
                </template>
              </div>
            </div>
          </list-scroll>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import navBar from '@/components/NavBar.vue'
import listScroll from '@/components/ListScroll.vue'
import { getCategory } from "@/service/good"
import { showLoadingToast, closeToast } from 'vant'
const router = useRouter()
// composition API 获取 refs 的形式
const searchWrap = ref(null)
const state = reactive({
  categoryData: [],
  currentIndex: 15
})

onMounted(async () => {
  let $screenHeight = document.documentElement.clientHeight
  console.log('searchWrap.value', searchWrap.value)
  searchWrap.value.style.height = $screenHeight - 100 + 'px'
  showLoadingToast('loading....')
  const { data } = await getCategory()
  closeToast()
  state.categoryData = data
})

const goHome = () => {
  router.push({ path: 'home' })
}

const selectMenu = (index) => {
  state.currentIndex = index
}

const selectProduct = (item) => {
  console.log('item', item.categoryId)
  router.push({ path: '/product-list', query: { categoryId: item.categoryId } })
}
</script>
<style lang="less" scoped>
@import '../common/style/mixin';

// 定义颜色变量
@primary: #FFA500; // 橙色
@secondary: #FFD700; // 黄色
@background: #FFF5E1; // 背景颜色（浅橙色）
@header-bg: #fff; // 头部背景颜色
@text-color: #232326; // 文字颜色
@inactive-text-color: #656771; // 非激活文字颜色
@active-bg: @primary; // 激活状态背景颜色
@border-color: rgba(0, 0, 0, 0.1); // 边框颜色

.categray {
  .category-header {
    background: @header-bg;
    position: fixed;
    left: 0;
    top: 0;
    .fj();
    .wh(100%, 60px); // 增加头部高度
    line-height: 60px; // 头部内容居中
    padding: 0 20px; // 增加内边距
    box-sizing: border-box;
    font-size: 16px; // 增加字体大小
    color: @inactive-text-color;
    z-index: 10000;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); // 添加阴影效果

    &.active {
      background: @active-bg;
      color: #fff; // 激活状态下文字颜色为白色
    }

    .icon-left {
      font-size: 26px; // 增加图标大小
      font-weight: bold;
    }

    .header-search {
      display: flex;
      width: 80%;
      height: 30px; // 增加搜索框高度
      line-height: 30px;
      margin: 10px 0;
      padding: 5px 10px; // 增加内边距
      color: @text-color;
      background: #F7F7F7;
      border-radius: 25px; // 改变圆角样式
      border: 1px solid @border-color; // 添加边框

      .nbSearch {
        padding: 0 10px 0 20px;
        font-size: 18px; // 增加字体大小
      }

      .search-title {
        font-size: 12px;
        color: #666;
        line-height: 30px; // 与搜索框对齐
      }
    }

    .icon-More {
      font-size: 22px; // 增加更多图标大小
    }
  }
}

.search-wrap {
  .fj();
  width: 100%;
  margin-top: 60px; // 增加顶部间距以适应新的头部高度
  background: @background;

  .nav-side-wrapper {
    width: 28%;
    height: 100%;
    overflow: hidden;

    .nav-side {
      width: 100%;
      .boxSizing();
      background: @background;

      li {
        width: 100%;
        height: 60px; // 增加列表项高度
        text-align: center;
        line-height: 60px; // 列表项居中
        font-size: 15px; // 增加字体大小
        transition: background 0.3s; // 添加过渡效果

        &:hover {
          background: rgba(255, 165, 0, 0.1); // 悬停效果
        }

        &.active {
          color: @primary;
          background: @header-bg;
        }
      }
    }
  }

  .search-content {
    width: 72%;
    height: 100%;
    padding: 10px; // 增加内边距
    background: @header-bg;
    overflow-y: scroll;
    touch-action: pan-y;

    * {
      touch-action: pan-y;
    }

    .boxSizing();

    .swiper-container {
      width: 100%;

      .swiper-slide {
        width: 100%;

        .category-main-img {
          width: 100%;
        }

        .category-list {
          display: flex;
          flex-wrap: wrap;
          flex-shrink: 0;
          width: 100%;

          .catogory-title {
            width: 100%;
            font-size: 18px; // 增加类别标题字体大小
            font-weight: 600; // 加粗类别标题
            padding: 25px 0; // 增加上下内边距
            color: @text-color; // 统一文字颜色
            // border-bottom: 2px solid @primary; // 移除下边框
          }

          .product-item {
            width: 33.3333%;
            margin-bottom: 15px; // 增加产品项底部间距
            text-align: center;
            font-size: 16px; // 增加字体大小

            .product-img {
              .wh(30px, 30px);
            }
          }
        }
      }
    }
  }
}

.fade-out-enter-active,
.fade-out-leave-active {
  // transition: opacity 0.5s;
}

.fade-out-enter,
.fade-out-leave-to {
  // opacity: 0;
}
</style>

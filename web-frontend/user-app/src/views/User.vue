<template>
  <div class="user-box">
    <s-header :name="'Person'"></s-header>
    <van-skeleton title :avatar="true" :row="3" :loading="state.loading">
      <div class="user-info">
        <div class="info">
          <img src="https://s.yezgea02.com/1604040746310/aaaddd.png"/>
          <div class="user-desc">
           <span>nickname：{{ state.user.nickName }}</span>
            <span>username：{{ state.user.loginName }}</span>
<!--            <span class="name">个性签名：{{ state.user.introduceSign }}</span>-->
          </div>
        </div>
      </div>
    </van-skeleton>
    <ul class="user-list">
      <li class="van-hairline--bottom" @click="goTo('/order')">
        <span>My Order</span>
        <van-icon name="arrow" />
      </li>
      <li class="van-hairline--bottom" @click="goTo('/setting')">
        <span>Account Manage</span>
        <van-icon name="arrow" />
      </li>
      <li class="van-hairline--bottom" @click="goTo('/address', { from: 'mine' })">
        <span>Address Manage</span>
        <van-icon name="arrow" />
      </li>
<!--      <li @click="goTo('/about')">
&lt;!&ndash;        <span>关于我们</span>&ndash;&gt;
&lt;!&ndash;        <van-icon name="arrow" />&ndash;&gt;
      </li>-->
    </ul>
    <nav-bar></nav-bar>
  </div>
</template>

<script setup>
import { reactive, onMounted, toRefs } from 'vue'
import navBar from '@/components/NavBar.vue'
import sHeader from '@/components/SimpleHeader.vue'
import { getUserInfo } from '@/service/user'
import { useRouter } from 'vue-router'
const router = useRouter()
const state = reactive({
  user: {},
  loading: true
})

onMounted(async () => {
  const { data } = await getUserInfo()
  state.user = data
  state.loading = false
})

const goBack = () => {
  router.go(-1)
}

const goTo = (r, query) => {
  router.push({ path: r, query: query || {} })
}
</script>

<style lang="less" scoped>
@import '../common/style/mixin';

.user-box {
  .user-header {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 10000;
    .fj();
    .wh(100%, 44px);
    line-height: 44px;
    padding: 0 10px;
    .boxSizing();
    color: #252525;
    background-color: #fff;
    border-bottom: 2px solid #f5a623; // 橙色边框
    box-shadow: 0px 2px 6px rgba(0, 0, 0, 0.1); // 添加阴影
    .user-name {
      font-size: 15px;
      font-weight: bold;
      color: #f7c04a; // 黄色标题文字
    }
  }

  .user-info {
    width: 94%;
    margin: 15px auto;
    height: 120px;
    background: linear-gradient(90deg, #f5a623, #f7c04a); // 橙色到黄色渐变背景
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.15); // 增强阴影效果
    border-radius: 12px; // 圆角更明显
    border: 1px solid #f5a623; // 添加边框
    display: flex;
    align-items: center;
    padding: 20px;

    .info {
      display: flex;
      width: 100%;
      height: 100%;
      img {
        .wh(70px, 70px); // 放大头像尺寸
        border-radius: 50%;
        border: 2px solid #fff; // 头像边框
      }

      .user-desc {
        display: flex;
        flex-direction: column;
        margin-left: 15px;
        font-size: 15px;
        color: #fff;
        font-weight: 500;

        span {
          color: #fff;
          font-size: 14px;
          margin-top: 5px;
        }
      }

      .account-setting {
        position: absolute;
        top: 15px;
        right: 15px;
        font-size: 14px;
        color: #fff;
        display: flex;
        align-items: center;
        cursor: pointer;
        transition: color 0.3s;

        &:hover {
          color: #ffe680; // 悬停变浅黄色
        }

        .van-icon-setting-o {
          font-size: 18px;
          margin-right: 5px;
        }
      }
    }
  }

  .user-list {
    padding: 0 25px;
    margin-top: 25px;

    li {
      height: 45px;
      line-height: 45px;
      display: flex;
      justify-content: space-between;
      font-size: 15px;
      padding: 5px 0;
      border-bottom: 1px solid #f7c04a; // 黄色分割线

      &:hover {
        background-color: #fff3e0; // 橙色浅背景悬停效果
      }

      .van-icon-arrow {
        font-size: 16px;
        color: #f5a623;
        margin-top: 12px;
      }
    }
  }
}
</style>

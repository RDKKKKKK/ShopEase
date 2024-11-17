<template>
  <div class="order-box">
    <s-header :name="'My Order'" :back="'/user'"></s-header>
    <van-tabs @click-tab="onChangeTab" :color="'#f7c04a'" :title-active-color="'#f7c04a'" class="order-tab" v-model="state.status">
      <van-tab title="All Order" name=''></van-tab>
      <van-tab title="To be payed" name="0"></van-tab>
      <van-tab title="To be confirmed" name="1"></van-tab>
      <van-tab title="To Be shiped" name="2"></van-tab>
      <van-tab title="Shiped" name="3"></van-tab>
      <van-tab title="Finished" name="4"></van-tab>
    </van-tabs>
    <div class="content">
      <van-pull-refresh v-model="state.refreshing" @refresh="onRefresh" class="order-list-refresh">
        <van-list
          v-model:loading="state.loading"
          :finished="state.finished"
          finished-text="No More"
          @load="onLoad"
          @offset="10"
        >
          <div v-for="(item, index) in state.list" :key="index" class="order-item-box" @click="goTo(item.orderNo)">
            <div class="order-item-header">
              <span>Order time：{{ item.createTime }}</span>
              <span>{{ item.orderStatusString }}</span>
            </div>
            <van-card
              v-for="one in item.newBeeMallOrderItemVOS"
              :key="one.orderId"
              :num="one.goodsCount"
              :price="one.sellingPrice"
              desc="Free Delivery"
              :title="one.goodsName"
              :thumb="$filters.prefix(one.goodsCoverImg)"
            />
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import sHeader from '@/components/SimpleHeader.vue'
import { getOrderList } from '@/service/order'
import { useRouter } from 'vue-router'

const router = useRouter()
const state = reactive({
  status: '',
  loading: false,
  finished: false,
  refreshing: false,
  list: [],
  page: 1,
  totalPage: 0
})

const loadData = async () => {
  const { data, data: { list } } = await getOrderList({ pageNumber: state.page, status: state.status })
  state.list = state.list.concat(list)
  state.totalPage = data.totalPage
  state.loading = false;
  if (state.page >= data.totalPage) state.finished = true
}

const onChangeTab = ({ name }) => {
  // 这里 Tab 最好采用点击事件，@click，如果用 @change 事件，会默认进来执行一次。
  state.status = name
  onRefresh()
}

const goTo = (id) => {
  router.push({ path: '/order-detail', query: { id } })
}

const goBack = () => {
  router.go(-1)
}

const onLoad = () => {
  if (!state.refreshing && state.page < state.totalPage) {
    console.log(state.page)
    console.log(state.totalPage)
    state.page = state.page + 1
  }
  if (state.refreshing) {
    state.list = [];
    state.refreshing = false;
  }
  loadData()
}

const onRefresh = () => {
  state.refreshing = true
  state.finished = false
  state.loading = true
  state.page = 1
  onLoad()
}
</script>

<style lang="less" scoped>
@import '../common/style/mixin';

.order-box {
  .order-header {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 10000;
    .fj();
    .wh(100%, 50px); /* 增加高度 */
    line-height: 50px;
    padding: 0 15px; /* 增加内边距 */
    .boxSizing();
    color: #4A4A4A; /* 暗灰色 */
    background: #fff;
    border-bottom: 2px solid #ffcc00; /* 黄色边框 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 加入阴影效果 */

    .order-name {
      font-size: 16px; /* 增大字体 */
      font-weight: bold; /* 加粗字体 */
      color: #ff8c00; /* 深橙色 */
    }
  }

  .order-tab {
    position: fixed;
    left: 0;
    z-index: 1000;
    width: 100%;
    background: #fff; /* 确保背景为白色 */
    border-bottom: 2px solid #ffcc00; /* 黄色边框 */
    padding: 5px 0; /* 上下内边距 */
    display: flex; /* 使用flex布局 */
    justify-content: space-around; /* 平均分配空间 */

    /* 每个选项 */
    .tab-item {
      font-size: 14px; /* 字体大小 */
      color: #ff8c00; /* 深橙色 */
      padding: 10px; /* 内边距 */
      border-radius: 5px; /* 圆角 */
      transition: background 0.3s; /* 背景变化动画 */

      &:hover {
        background: rgba(255, 140, 0, 0.1); /* 橙色hover效果 */
      }
    }
  }

  .skeleton {
    margin-top: 70px; /* 增加顶部间距以适应新的header高度 */
  }

  .content {
    height: calc(~"(100vh - 80px)"); /* 更新高度计算 */
    overflow: hidden;
    overflow-y: scroll;
    margin-top: 34px;
    background: #fdf6e3; /* 浅黄色背景 */
    padding: 10px; /* 内容内边距 */

    .order-list-refresh {
      .van-card__content {
        display: flex;
        flex-direction: column;
        justify-content: center;
        background-color: #fff; /* 订单列表卡片背景 */
        border-radius: 10px; /* 圆角 */
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 加入阴影效果 */
        margin: 10px 0; /* 增加上下间距 */
      }

      .van-pull-refresh__head {
        background: #fff5e6; /* 浅橙色背景 */
        color: #ff8c00; /* 深橙色文本 */
      }

      .order-item-box {
        margin: 20px 10px;
        background-color: #fff; /* 保持卡片白色背景 */
        border-radius: 10px; /* 圆角 */

        .order-item-header {
          padding: 15px 20px; /* 增加内边距 */
          display: flex;
          justify-content: space-between;
          border-bottom: 1px solid #ffcc00; /* 下边框为黄色 */
        }

        .van-card {
          background-color: #fff;
          margin-top: 0;
          border-radius: 10px; /* 圆角 */
        }
      }
    }
  }
}
</style>


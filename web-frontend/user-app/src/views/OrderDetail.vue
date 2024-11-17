<template>
  <div class="order-detail-box">
    <s-header :name="'Order Detail'" @callback="close"></s-header>
    <div class="order-status">
      <div class="status-item">
        <label>Status:</label>
        <span>{{ state.detail.orderStatusString }}</span>
      </div>
      <div class="status-item">
        <label>No:</label>
        <span>{{ state.detail.orderNo }}</span>
      </div>
      <div class="status-item">
        <label>Order Time：</label>
        <span>{{ state.detail.createTime }}</span>
      </div>
      <van-button v-if="state.detail.orderStatus == 3" style="margin-bottom: 10px" color="#1baeae" block
        @click="handleConfirmOrder(state.detail.orderNo)">Recieved</van-button>
      <van-button v-if="state.detail.orderStatus == 0" style="margin-bottom: 10px" color="#1baeae" block
        @click="showPayFn">Go to Payment</van-button>
      <van-button v-if="!(state.detail.orderStatus < 0 || state.detail.orderStatus == 4)" block
        @click="handleCancelOrder(state.detail.orderNo)">Cancel Order</van-button>
    </div>
    <div class="order-price">
      <div class="price-item">
        <label>Price:</label>
        <span>${{ state.detail.totalPrice }}</span>
      </div>
      <div class="price-item">
        <label>Delivery:</label>
        <span>Door-Step</span>
      </div>
    </div>
    <van-card v-for="item in state.detail.newBeeMallOrderItemVOS" :key="item.goodsId" style="background: #fff"
      :num="item.goodsCount" :price="item.sellingPrice" desc="Free DElivery" :title="item.goodsName"
      :thumb="$filters.prefix(item.goodsCoverImg)" />
    <van-popup v-model:show="state.showPay" position="bottom" :style="{ height: '24%' }">
      <div :style="{ width: '90%', margin: '0 auto', padding: '20px 0' }">
        <van-button :style="{ marginBottom: '10px' }" color="#1989fa" block
          @click="handlePayOrder(state.detail.orderNo, 1)">AliPay</van-button>
        <van-button color="#4fc08d" block @click="handlePayOrder(state.detail.orderNo, 2)">WechatPay</van-button>
      </div>
    </van-popup>
  </div>
</template>

<script setup>
import { reactive, toRefs, onMounted } from 'vue'
import sHeader from '@/components/SimpleHeader.vue'
import { getOrderDetail, cancelOrder, confirmOrder, payOrder } from '@/service/order'
import { showConfirmDialog, showLoadingToast, closeToast, showSuccessToast, closeDialog } from 'vant'
import { useRoute } from 'vue-router'
const route = useRoute()
const state = reactive({
  detail: {},
  showPay: false
})

onMounted(() => {
  init()
})

const init = async () => {
  showLoadingToast({
    message: 'Loading...',
    forbidClick: true
  });
  const { id } = route.query
  const { data } = await getOrderDetail(id)
  state.detail = data
  closeToast()
}

const handleCancelOrder = (id) => {
  showConfirmDialog({
    title: 'Are you sure to cancel?',
  }).then(() => {
    cancelOrder(id).then(res => {
      if (res.resultCode == 200) {
        showSuccessToast('Delete Success')
        init()
      }
    })
  }).catch(() => {
    // on cancel
  });
}

const handleConfirmOrder = (id) => {
  showConfirmDialog({
    title: 'Are you sure to cancel?',
  }).then(() => {
    confirmOrder(id).then(res => {
      if (res.resultCode == 200) {
        showSuccessToast('Success')
        init()
      }
    })
  }).catch(() => {
    // on cancel
  });
}

const showPayFn = () => {
  state.showPay = true
}

const handlePayOrder = async (id, type) => {
  await payOrder({ orderNo: id, payType: type })
  state.showPay = false
  init()
}

const close = () => {
  closeDialog
}
</script>

<style lang="less" scoped>
.order-detail-box {
  background: #fffbe6;
  /* 淡黄色背景 */
  padding: 15px;
  border-radius: 10px;
  /* 添加圆角 */

  .order-status {
    background: #fff;
    padding: 20px;
    margin-bottom: 15px;
    font-size: 16px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    /* 添加阴影 */

    .status-item {
      margin-bottom: 15px;
      display: flex;
      justify-content: space-between;

      label {
        color: #ff8c00;
        /* 深橙色 */
        font-weight: 600;
      }

      span {
        color: #333;
      }
    }
  }

  .order-price {
    background: #fff;
    margin: 20px 0;
    padding: 20px;
    font-size: 16px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    /* 添加阴影 */

    .price-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 15px;

      label {
        color: #ff8c00;
        font-weight: 600;
      }

      span {
        color: #333;
      }
    }
  }

  .van-card {
    background: #fff;
    border-radius: 8px;
    margin-top: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    /* 添加阴影 */

    .van-card__content {
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
  }
}
</style>

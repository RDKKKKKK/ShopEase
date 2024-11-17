

<template>
  <div class="product-detail">
    <s-header :name="'Product Detail'"></s-header>
    <div class="detail-content">
      <div class="detail-swipe-wrap">
        <van-swipe class="my-swipe" indicator-color="#1baeae">
          <van-swipe-item v-for="(item, index) in state.detail.goodsCarouselList" :key="index">
            <img :src="item" alt="">
          </van-swipe-item>
        </van-swipe>
      </div>
      <div class="product-info">
        <div class="product-title">
          {{ state.detail.goodsName || '' }}
        </div>
        <div class="product-desc">Free Delivery</div>
        <div class="product-price">
          <span>¥{{ state.detail.sellingPrice || '' }}</span>
          <!-- <span>库存203</span> -->
        </div>
      </div>
      <div class="product-intro">
        <ul>
          <li>Detail</li>
<!--          <li>参数</li>
          <li>安装服务</li>
          <li>常见问题</li>-->
        </ul>
        <div class="product-content" v-html="state.detail.goodsDetailContent || ''"></div>
      </div>
    </div>
    <van-action-bar>
<!--      <van-action-bar-icon icon="chat-o" text="客服" />-->
      <van-action-bar-icon icon="cart-o" :badge="!cart.count ? '' : cart.count" @click="goTo()" text="Cart" />
      <van-action-bar-button type="warning" @click="handleAddCart" text="Add to Cart" />
      <van-action-bar-button type="danger" @click="goToCart" text="Buy" />
    </van-action-bar>
  </div>
</template>

<script setup>
import { reactive, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { getDetail } from '@/service/good'
import { addCart } from '@/service/cart'
import sHeader from '@/components/SimpleHeader.vue'
import { showSuccessToast } from 'vant'
import { prefix } from '@/common/js/utils'
const route = useRoute()
const router = useRouter()
const cart = useCartStore()

const state = reactive({
  detail: {
    goodsCarouselList: []
  }
})

onMounted(async () => {
  const { id } = route.params
  const { data } = await getDetail(id)
  data.goodsCarouselList = data.goodsCarouselList.map(i => prefix(i))
  state.detail = data
  cart.updateCart()
})

nextTick(() => {
  // 一些和DOM有关的东西
  const content = document.querySelector('.detail-content')
  content.scrollTop = 0
})

const goBack = () => {
  router.go(-1)
}

const goTo = () => {
  router.push({ path: '/cart' })
}

const handleAddCart = async () => {
  const { resultCode } = await addCart({ goodsCount: 1, goodsId: state.detail.goodsId })
  if (resultCode == 200 ) showSuccessToast('Add Successful')
  cart.updateCart()
}

const goToCart = async () => {
  await addCart({ goodsCount: 1, goodsId: state.detail.goodsId })
  cart.updateCart()
  router.push({ path: '/cart' })
}

</script>

<style lang="less">
@import '../common/style/mixin';

.product-detail {
  .detail-header {
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
    background: #fff;
    border-bottom: 1px solid #ffd27f; /* 边框调整为浅橙色 */
    .product-name {
      font-size: 14px;
    }
  }

  .detail-content {
    height: calc(100vh - 50px);
    overflow: hidden;
    overflow-y: auto;

    .detail-swipe-wrap {
      .my-swipe .van-swipe-item {
        img {
          width: 100%;
        }
      }
    }

    .product-info {
      padding: 0 10px;

      .product-title {
        font-size: 18px;
        text-align: left;
        color: #040303; /* 橙色标题 */
      }

      .product-desc {
        font-size: 14px;
        text-align: left;
        color: #040303; /* 橙色描述 */
        padding: 5px 0;
      }

      .product-price {
        .fj();
        span:nth-child(1) {
          color: #ff4500; /* 深橙色价格 */
          font-size: 22px;
        }

        span:nth-child(2) {
          color: #ffd700; /* 金黄色折扣价 */
          font-size: 16px;
        }
      }
    }

    .product-intro {
      width: 100%;
      padding-bottom: 50px;

      ul {
        .fj();
        width: 100%;
        margin: 10px 0;

        li {
          flex: 1;
          padding: 5px 0;
          text-align: center;
          font-size: 15px;
          border-right: 1px solid #ffa500; /* 橙色分隔线 */
          box-sizing: border-box;

          &:last-child {
            border-right: none;
          }
        }
      }

      .product-content {
        padding: 0 20px;

        img {
          width: 100%;
        }
      }
    }
  }

  /* 操作按钮颜色 */
  .van-action-bar-button--warning {
    background: linear-gradient(to right, #ffae42, #ffd700); /* 橙黄渐变 */
  }

  .van-action-bar-button--danger {
    background: linear-gradient(to right, #ff8c00, #ff4500); /* 深橙渐变 */
  }
}
</style>

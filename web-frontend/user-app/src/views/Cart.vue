

<template>
  <div class="cart-box">
    <s-header :name="'Cart'" :noback="true"></s-header>
    <div class="cart-body">
      <van-checkbox-group @change="groupChange" v-model="state.result" ref="checkboxGroup">
        <van-swipe-cell :right-width="50" v-for="(item, index) in state.list" :key="index">
          <div class="good-item">
            <van-checkbox :name="item.cartItemId" />
            <div class="good-img"><img :src="$filters.prefix(item.goodsCoverImg)" alt=""></div>
            <div class="good-desc">
              <div class="good-title">
                <span>{{ item.goodsName }}</span>
                <span>x{{ item.goodsCount }}</span>
              </div>
              <div class="good-btn">
                <div class="price">${{ item.sellingPrice }}</div>
                <van-stepper
                  integer
                  :min="1"
                  :max="5"
                  :model-value="item.goodsCount"
                  :name="item.cartItemId"
                  async-change
                  @change="onChange"
                />
              </div>
            </div>
          </div>
          <template #right>
            <van-button
              square
              icon="delete"
              type="danger"
              class="delete-button"
              @click="deleteGood(item.cartItemId)"
            />
          </template>
        </van-swipe-cell>
      </van-checkbox-group>
    </div>
    <van-submit-bar
      v-if="state.list.length > 0"
      class="submit-all van-hairline--top"
      :price="total * 100"
      label="    "
      currency="$"
      button-text="Check out"
      button-type="primary"
      @submit="onSubmit"
    >
      <van-checkbox @click="allCheck" v-model:checked="state.checkAll">Select all</van-checkbox>
    </van-submit-bar>
    <div class="empty" v-if="!state.list.length">
      <img class="empty-cart" src="https://s.yezgea02.com/1604028375097/empty-car.png" alt="Empty Cart">
      <div class="title">Empty</div>
      <van-button round color="#FFA500" type="primary" @click="goTo" block>Buy now</van-button>
    </div>
    <nav-bar></nav-bar>
  </div>
</template>

<script setup>
import { reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { showToast, showLoadingToast, closeToast, showFailToast } from 'vant'
import navBar from '@/components/NavBar.vue'
import sHeader from '@/components/SimpleHeader.vue'
import { getCart, deleteCartItem, modifyCart } from '@/service/cart'

const router = useRouter()
const cart = useCartStore()
const state = reactive({
  checked: false,
  list: [],
  all: false,
  result: [],
  checkAll: true
})

onMounted(() => {
  init()
})

const init = async () => {
  showLoadingToast({ message: 'Loading...', forbidClick: true });
  const { data } = await getCart({ pageNumber: 1 })
  state.list = data
  state.result = data.map(item => item.cartItemId)
  closeToast()
}

const total = computed(() => {
  let sum = 0
  let _list = state.list.filter(item => state.result.includes(item.cartItemId))
  _list.forEach(item => {
    sum += item.goodsCount * item.sellingPrice
  })
  return sum
})

const goBack = () => {
  router.go(-1)
}

const goTo = () => {
  router.push({ path: '/home' })
}

const onChange = async (value, detail) => {
  if (value > 5) {
    showFailToast('Exceeds the maximum purchase quantity for a single item')
    return
  }
  if (value < 1) {
    showFailToast('The quantity of items cannot be less than 0')
    return
  }
  /**
   * 这里的操作是因为，后面修改购物车后，手动添加的计步器的数据，为了防止数据不对
   * 这边做一个拦截处理，如果点击的时候，购物车单项的 goodsCount 等于点击的计步器数字，
   * 那么就不再进行修改操作
  */
  if (state.list.find(item => item.cartItemId == detail.name)?.goodsCount == value) return
  showLoadingToast({ message: 'Modifying...', forbidClick: true });
  const params = {
    cartItemId: detail.name,
    goodsCount: value
  }
  await modifyCart(params)
  /**
   * 修改完成后，没有请求购物车列表，是因为闪烁的问题，
   * 这边手动给操作的购物车商品修改数据
  */
  state.list.forEach(item => {
    if (item.cartItemId == detail.name) {
      item.goodsCount = value
    }
  })
  closeToast()
}

const onSubmit = async () => {
  if (state.result.length == 0) {
    showFailToast('Please select products for checkout')
    return
  }
  const params = JSON.stringify(state.result)
  router.push({ path: '/create-order', query: { cartItemIds: params } })
}

const deleteGood = async (id) => {
  await deleteCartItem(id)
  cart.updateCart()
  init()
}

const groupChange = (result) => {
  if (result.length == state.list.length) {
    state.checkAll = true
  } else {
    state.checkAll = false
  }
  state.result = result
}

const allCheck = () => {
  if (!state.checkAll) {
    state.result = state.list.map(item => item.cartItemId)
  } else {
    state.result = []
  }
}
</script>

<style lang="less">
@import '../common/style/mixin';

.cart-box {
  .cart-header {
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
    border-bottom: 1px solid #f5a623; // 橙色下边框
    box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); // 添加阴影
    .cart-name {
      font-size: 14px;
    }
  }

  .cart-body {
    margin: 16px 0 100px 0;
    padding-left: 10px;
    .good-item {
      display: flex;
      margin-bottom: 15px;
      border: 1px solid #f7c04a; // 黄色边框
      border-radius: 8px; // 圆角效果
      box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1); // 添加阴影
      overflow: hidden;

      .good-img {
        img {
          .wh(100px, 100px);
          border-right: 1px solid #f5a623; // 橙色分割线
        }
      }

      .good-desc {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        flex: 1;
        padding: 20px;

        .good-title {
          display: flex;
          justify-content: space-between;
          font-weight: bold;
          color: #f5a623; // 橙色标题
        }

        .good-btn {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .price {
            font-size: 18px;
            color: red;
            line-height: 28px;
          }

          .van-icon-delete {
            font-size: 22px;
            color: #f7c04a; // 黄色删除图标
            margin-top: 4px;
            cursor: pointer;
            transition: color 0.3s;
            &:hover {
              color: #f5a623; // 橙色高亮
            }
          }
        }
      }
    }

    .delete-button {
      width: 50px;
      height: 100%;
    }
  }

  .empty {
    width: 50%;
    margin: 0 auto;
    text-align: center;
    margin-top: 200px;

    .empty-cart {
      width: 150px;
      margin-bottom: 20px;
    }

    .van-icon-smile-o {
      font-size: 50px;
      color: #f7c04a; // 黄色表情
    }

    .title {
      font-size: 16px;
      margin-bottom: 20px;
      color: #f5a623; // 橙色文字
    }
  }

  .submit-all {
    margin-bottom: 64px;
    padding: 10px;
    border-top: 1px solid #f5a623; // 橙色分割线
    background-color: #fff;
    box-shadow: 0px -2px 5px rgba(0, 0, 0, 0.1); // 底部阴影

    .van-checkbox {
      margin-left: 10px;
    }

    .van-submit-bar__text {
      margin-right: 10px;
    }

    .van-submit-bar__button {
      background: #f5a623; // 橙色按钮
      color: #fff;
      border-radius: 20px;
      &:hover {
        background: #f7c04a; // 鼠标悬停变为黄色
      }
    }
  }

  .van-checkbox__icon--checked .van-icon {
    background-color: #f5a623; // 选中时橙色背景
    border-color: #f5a623;
  }
}
</style>

<template>
  <van-swipe class="my-swipe" :autoplay="3000" indicator-color="#1baeae">
    <van-swipe-item v-for="(item, index) in list" :key="index">
      <img :src="item.carouselUrl" alt="">
    </van-swipe-item>
  </van-swipe>
</template>

<script>
export default {
  props: {
    list: Array
  },
  methods: {
    goTo(url) {
      window.open(url)
    }
  }
}
</script>

<style lang='less' scoped>
.my-swipe {
  padding-top: 50px;
  margin-bottom: -25px;

  img {
    width: 100%;
    height: 100%;
  }
}
</style>

<template>
  <div class="seting-box">
    <s-header :name="'Account Manage'"></s-header>
    <div class="input-item">
      <van-field
          v-model="state.nickName"
          label="Nickname"
          class="custom-field"
      />
      <!-- <van-field v-model="state.introduceSign" label="个性签名" /> -->
      <van-field
          v-model="state.password"
          type="password"
          label="Password"
          class="custom-field"
      />
    </div>
    <van-button
        round
        class="save-btn save"
        type="primary"
        @click="save"
        block
    >
      Save
    </van-button>
    <van-button
        round
        class="save-btn logout"
        type="primary"
        @click="handleLogout"
        block
    >
      Log Out
    </van-button>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import md5 from 'js-md5'
import sHeader from '@/components/SimpleHeader.vue'
import { getUserInfo, EditUserInfo, logout } from '@/service/user'
import { setLocal } from '@/common/js/utils'
import { showSuccessToast } from 'vant'

const state = reactive({
  nickName: '',
  introduceSign: '',
  password: ''
})

onMounted(async () => {
  const { data } = await getUserInfo()
  state.nickName = data.nickName
  state.introduceSign = data.introduceSign
})

const save = async () => {
  const params = {
    introduceSign: state.introduceSign,
    nickName: state.nickName
  }
  if (state.password) {
    params.passwordMd5 = md5(state.password)
  }
  await EditUserInfo(params)
  showSuccessToast('Save success')
}

const handleLogout = async () => {
  const { resultCode } = await logout()
  if (resultCode === 200) {
    setLocal('token', '')
    window.location.href = '/home'
  }
}
</script>

<style lang="less" scoped>
.seting-box {
  .input-item {
    margin: 20px 0;
    padding: 10px;

    .custom-field {
      margin-bottom: 15px;
      border: 1px solid #ffa500; /* 橙色边框 */
      border-radius: 8px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
      padding: 8px;
      transition: box-shadow 0.3s, border-color 0.3s;

      &:focus-within {
        border-color: #ffd700; /* 输入时黄色边框 */
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 增强阴影效果 */
      }
    }
  }

  .save-btn {
    width: 80%;
    margin: 20px auto;
    color: white;

    &.save {
      background-color: #ffa500; /* 橙色 */
    }

    &.logout {
      background-color: #ffd700; /* 黄色 */
    }

    &:hover {
      opacity: 0.8; /* 悬停效果 */
    }
  }
}
</style>

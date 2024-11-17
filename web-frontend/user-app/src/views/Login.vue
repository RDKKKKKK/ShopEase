
<template>
  <div class="login">
    <s-header :name="type == 'login' ? 'Login' : 'Register'" :back="'/home'"></s-header>
    <img class="logo" src="https://s.yezgea02.com/1604045825972/shopease-mall-vue3-app-logo.png" alt="">
    <div v-if="state.type == 'login'" class="login-body login">
      <van-form @submit="onSubmit">
        <van-field
          v-model="state.username"
          name="username"
          label="Username"
          placeholder="Username"
          :rules="[{ required: true, message: 'Input Username' }]"
        />
        <van-field
          v-model="state.password"
          type="password"
          name="password"
          label="password"
          placeholder="password"
          :rules="[{ required: true, message: 'Input password' }]"
        />
        <van-field
          center
          clearable
          label="captcha"
          placeholder="Input captcha"
          v-model="state.verify"
        >
          <template #button>
            <vue-img-verify ref="verifyRef" />
          </template>
        </van-field>
        <div style="margin: 16px;">
          <div class="link-register" @click="toggle('register')">Register Now</div>
          <van-button round block color="#FFCC80" native-type="submit">Login</van-button>
        </div>
      </van-form>
    </div>
    <div v-else class="login-body register">
      <van-form @submit="onSubmit">
        <van-field
          v-model="state.username1"
          name="username1"
          label="username"
          placeholder="username1"
          :rules="[{ required: true, message: 'Input username1' }]"
        />
        <van-field
          v-model="state.password1"
          type="password"
          name="password1"
          label="password"
          placeholder="password"
          :rules="[{ required: true, message: 'Input password' }]"
        />
        <van-field
          center
          clearable
          label="captcha"
          placeholder="Input captcha"
          v-model="state.verify"
        >
          <template #button>
            <vue-img-verify ref="verifyRef" />
          </template>
        </van-field>
        <div style="margin: 16px;">
          <div class="link-login" @click="toggle('login')">Have an Account</div>
          <van-button round block color="#1baeae" native-type="submit">Register</van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import sHeader from '@/components/SimpleHeader.vue'
import vueImgVerify from '@/components/VueImageVerify.vue'
import { login, register } from '@/service/user'
import { setLocal } from '@/common/js/utils'
import md5 from 'js-md5'
import { showSuccessToast, showFailToast } from 'vant'
const verifyRef = ref(null)
const state = reactive({
  username: '',
  password: '',
  username1: '',
  password1: '',
  type: 'login',
  imgCode: '',
  verify: ''
})

// 切换登录和注册两种模式
const toggle = (v) => {
  state.type = v
  state.verify = ''
}

// 提交登录或注册表单
const onSubmit = async (values) => {
  state.imgCode = verifyRef.value.state.imgCode || ''
  if (state.verify.toLowerCase() != state.imgCode.toLowerCase()) {
    showFailToast('Wrong captcha')
    return
  }
  if (state.type == 'login') {
    const { data } = await login({
      "loginName": values.username,
      "passwordMd5": md5(values.password)
    })
    setLocal('token', data)
    // 需要刷新页面，否则 axios.js 文件里的 token 不会被重置
    window.location.href = '/'
  } else {
    await register({
      "loginName": values.username1,
      "password": values.password1
    })
    showSuccessToast('Register Success')
    state.type = 'login'
    state.verify = ''
  }
}
</script>

<style lang="less">
.login {
  .logo {
    width: 100px;
    height: 100px;
    display: block;
    margin: 60px auto 10px;
    border-radius: 50%;
    background-color: #f7c04a; // 橙黄色背景
  }

  .login-body {
    padding: 10px 30px;
    background-color: #FFF3E0;
    border-radius: 12px;
    // 不添加阴影
  }

  .login, .register {
    .link-register, .link-login {
      font-size: 16px;
      margin-bottom: 15px;
      color: #FF9800;
      font-weight: bold;
      display: block;
      text-align: center;
    }
  }

  .van-field {
    margin-bottom: 16px;

    // 为每个输入框添加单独的阴影效果
    .van-field__control {
      border: 1px solid #FFCC80;
      border-radius: 8px;
      padding-left: 10px;
      height: 42px;
      font-size: 18px;
      transition: box-shadow 0.3s, border-color 0.3s;

      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); // 默认阴影

      &:focus {
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.25); // 聚焦时加深阴影
        border-color: #FF9800;
      }
    }
  }

  .verify-bar-area {
    margin-top: 30px;

    .verify-left-bar {
      border-color: #FFB300;
    }

    .verify-move-block {
      background-color: #FF9800;
      color: #FFF;
      border-radius: 4px;
    }
  }

  .verify {
    display: flex;
    justify-content: space-between;
    align-items: center;

    > div {
      width: 48%;
    }

    .cerify-code-panel {
      margin-top: 12px;
    }

    .verify-code {
      width: 45%!important;
      float: none!important;
    }

    .verify-code-area {
      width: 50%!important;
      margin-left: 10px!important;

      .varify-input-code {
        width: 100%;
        height: 42px;
        border: 1px solid #FFCC80;
        border-radius: 8px;
        padding-left: 10px;
        font-size: 18px;
        transition: box-shadow 0.3s, border-color 0.3s;

        // 输入框默认阴影
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);

        &:focus {
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.25);
          border-color: #FF9800;
        }
      }

      .verify-change-area {
        line-height: 44px;
        text-align: center;
        color: #FF5722;
        cursor: pointer;
      }
    }
  }
}
</style>




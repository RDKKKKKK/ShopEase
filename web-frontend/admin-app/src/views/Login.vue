<template>
  <div class="login-body">
    <div class="login-container">
      <div class="head">
        <div class="name">
          <div class="title">ShopEase</div>
          <div class="tips">Backend Management</div>
        </div>
      </div>
      <el-form label-position="left" :rules="state.rules" :model="state.ruleForm" ref="loginForm" class="login-form">
        <el-form-item label="Username" prop="username">
          <el-input type="text" v-model.trim="state.ruleForm.username" autocomplete="off"
            placeholder="Enter your username"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input type="password" v-model.trim="state.ruleForm.password" autocomplete="off"
            placeholder="Enter your password" style="padding-left: 5px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="submitForm">
            Sign in
          </el-button>
          <el-checkbox v-model="state.checked" class="auto-login-checkbox">
            Auto-login next time
          </el-checkbox>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import axios from '@/utils/axios'
import md5 from 'js-md5'
import { reactive, ref } from 'vue'
import { localSet } from '@/utils'

const loginForm = ref(null)
const state = reactive({
  ruleForm: {
    username: '',
    password: ''
  },
  checked: true,
  rules: {
    username: [
      { required: true, message: 'Username cannot be empty', trigger: 'blur' }
    ],
    password: [
      { required: true, message: 'Password cannot be empty', trigger: 'blur' }
    ]
  }
})

const submitForm = async () => {
  loginForm.value.validate((valid) => {
    if (valid) {
      axios.post('/users/admin/login', {
        userName: state.ruleForm.username || '',
        passwordMd5: md5(state.ruleForm.password)
      }).then(res => {
        localSet('token', res)
        window.location.href = '/'
      })
    } else {
      console.log('Error in form submission!')
      return false
    }
  })
}

const resetForm = () => {
  loginForm.value.resetFields()
}
</script>

<style scoped>
.login-body {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background-color: #f0f0f0;
}

.login-container {
  width: 380px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.head {
  text-align: center;
  margin-bottom: 20px;
}

.head .title {
  font-size: 24px;
  font-weight: 700;
  color: #333;
}

.head .tips {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.login-form {
  width: 100%;
}

.login-form .el-form-item {
  margin-bottom: 15px;
}

.login-form .el-input {
  border-color: #ccc;
}

.el-button {
  background-color: #333;
  border-color: #333;
  color: #fff;
  font-weight: bold;
}

.el-button:hover {
  background-color: #555;
  border-color: #555;
}

.auto-login-checkbox {
  color: #444;
  margin-top: 10px;
  display: flex;
  justify-content: center;
}

.el-checkbox__input {
  border-color: #888;
}
</style>

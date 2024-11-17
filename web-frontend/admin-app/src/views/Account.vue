<template>
  <el-card class="account-container">
    <el-form :model="state.nameForm" :rules="state.rules" ref="nameRef" label-width="150px" label-position="right"
      class="demo-ruleForm">
      <el-form-item label="Login Name:" prop="loginName">
        <el-input v-model="state.nameForm.loginName" placeholder="Enter your login name" clearable></el-input>
      </el-form-item>
      <el-form-item label="Nickname:" prop="nickName">
        <el-input v-model="state.nameForm.nickName" placeholder="Enter your nickname" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitName" class="submit-button">Save Changes</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card class="account-container">
    <el-form :model="state.passForm" :rules="state.rules" ref="passRef" label-width="150px" label-position="right"
      class="demo-ruleForm">
      <el-form-item label="Current Password:" prop="oldpass">
        <el-input v-model="state.passForm.oldpass" type="password" placeholder="Enter your current password"
          clearable></el-input>
      </el-form-item>
      <el-form-item label="New Password:" prop="newpass">
        <el-input v-model="state.passForm.newpass" type="password" placeholder="Enter your new password"
          clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitPass" class="submit-button">Save Changes</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import md5 from 'js-md5'

const nameRef = ref(null)
const passRef = ref(null)
const state = reactive({
  user: null,
  nameForm: {
    loginName: '',
    nickName: ''
  },
  passForm: {
    oldpass: '',
    newpass: ''
  },
  rules: {
    loginName: [
      { required: true, message: 'Login name cannot be empty', trigger: ['change'] }
    ],
    nickName: [
      { required: true, message: 'Nickname cannot be empty', trigger: ['change'] }
    ],
    oldpass: [
      { required: true, message: 'Current password cannot be empty', trigger: ['change'] }
    ],
    newpass: [
      { required: true, message: 'New password cannot be empty', trigger: ['change'] }
    ]
  },
})
onMounted(() => {
  axios.post('/users/admin/profile').then(res => {
    state.user = res
    state.nameForm.loginName = res.loginUserName
    state.nameForm.nickName = res.nickName
  })
})
const submitName = () => {
  nameRef.value.validate((valid) => {
    if (valid) {
      axios.put('/users/admin/name', {
        loginUserName: state.nameForm.loginName,
        nickName: state.nameForm.nickName
      }).then(() => {
        ElMessage.success('Changes saved successfully')
        window.location.reload()
      })
    }
  })
}
const submitPass = () => {
  passRef.value.validate((valid) => {
    if (valid) {
      axios.put('/users/admin/password', {
        originalPassword: md5(state.passForm.oldpass),
        newPassword: md5(state.passForm.newpass)
      }).then(() => {
        ElMessage.success('Changes saved successfully')
        window.location.reload()
      })
    }
  })
}
</script>

<style>
.account-container {
  margin-bottom: 20px;
  background-color: #ffffff;
  /* White background for cards */
  border-radius: 8px;
  /* Softer corners */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  /* Softer shadow for depth */
  padding: 15px;
  /* Reduced padding for compactness */
  max-width: 400px;
  /* Set a max width for the cards */
}

.demo-ruleForm {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  /* Modern font */
}

.el-form-item {
  margin-bottom: 15px;
  /* Spacing between form items */
}

.submit-button {
  width: 100%;
  /* Full-width buttons for better UX */
  background-color: #040303;
  /* Primary button color */
  color: white;
  /* White text color */
  font-size: 14px;
  /* Adjusted font size for compact design */
  transition: background-color 0.3s, transform 0.2s;
  /* Smooth transition for background and hover effects */
}

.submit-button:hover {
  background-color: #040303;
  /* Lighter shade on hover */
  transform: translateY(-1px);
  /* Slight lift effect on hover */
}

.el-input {
  border: 1px solid #dcdfe6;
  /* Light border */
  border-radius: 4px;
  /* Slight rounding of input corners */
  transition: border-color 0.3s;
  /* Smooth transition for border color */
}

.el-input:focus {
  border-color: #040303;
  /* Change border color on focus */
}
</style>

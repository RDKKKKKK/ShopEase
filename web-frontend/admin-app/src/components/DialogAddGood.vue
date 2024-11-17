<template>
  <el-dialog :title="type == 'add' ? 'Add Product' : 'Edit Product'" v-model="state.visible" width="450px">
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="120px" class="product-form">
      <el-form-item label="Product Name" prop="name">
        <el-input type="text" v-model="state.ruleForm.name" placeholder="Enter product name"></el-input>
      </el-form-item>
      <el-form-item label="Redirect Link" prop="link">
        <el-input type="text" v-model="state.ruleForm.link" placeholder="Enter redirect link"></el-input>
      </el-form-item>
      <el-form-item label="Product ID" prop="id">
        <el-input type="number" min="0" v-model="state.ruleForm.id" placeholder="Enter product ID"></el-input>
      </el-form-item>
      <el-form-item label="Sort Order" prop="sort">
        <el-input type="number" v-model="state.ruleForm.sort" placeholder="Enter sort order"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="state.visible = false" type="default">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Confirm</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'

const props = defineProps({
  type: String,
  configType: Number,
  reload: Function
})
const formRef = ref(null)
const state = reactive({
  visible: false,
  ruleForm: {
    name: '',
    link: '',
    id: '',
    sort: ''
  },
  rules: {
    name: [
      { required: true, message: 'Product name is required', trigger: 'blur' }
    ],
    id: [
      { required: true, message: 'Product ID is required', trigger: 'blur' }
    ],
    sort: [
      { required: true, message: 'Sort order is required', trigger: 'blur' }
    ]
  },
  id: ''
})

// Fetch product details
const getDetail = (id) => {
  axios.get(`/indexConfigs/admin/detail/${id}`).then(res => {
    state.ruleForm = {
      name: res.configName,
      id: res.goodsId,
      link: res.redirectUrl,
      sort: res.configRank
    }
  })
}

// Open dialog
const open = (id) => {
  state.visible = true
  if (id) {
    state.id = id
    getDetail(id)
  } else {
    state.ruleForm = {
      name: '',
      id: '',
      link: '',
      sort: ''
    }
  }
}

// Close dialog
const close = () => {
  state.visible = false
}

const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (state.ruleForm.id < 0 || state.ruleForm.id > 200) {
        ElMessage.error('Product ID must be between 0 and 200')
        return
      }
      if (props.type == 'add') {
        axios.post('/indexConfigs/admin/add', {
          configType: props.configType || 3,
          configName: state.ruleForm.name,
          redirectUrl: state.ruleForm.link,
          goodsId: state.ruleForm.id,
          configRank: state.ruleForm.sort
        }).then(() => {
          ElMessage.success('Product added successfully')
          state.visible = false
          if (props.reload) props.reload()
        })
      } else {
        axios.put('/indexConfigs/admin/update', {
          configId: state.id,
          configType: props.configType || 3,
          configName: state.ruleForm.name,
          redirectUrl: state.ruleForm.link,
          goodsId: state.ruleForm.id,
          configRank: state.ruleForm.sort
        }).then(() => {
          ElMessage.success('Product updated successfully')
          state.visible = false
          if (props.reload) props.reload()
        })
      }
    }
  })
}

defineExpose({ open, close })
</script>

<style scoped>
.product-form {
  background-color: #f9f9f9;
  border-radius: 5px;
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>

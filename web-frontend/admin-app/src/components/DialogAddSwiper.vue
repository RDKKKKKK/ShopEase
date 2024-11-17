<template>
  <el-dialog :title="type == 'add' ? 'Add Carousel' : 'Edit Carousel'" v-model="state.visible" width="450px">
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="120px" class="custom-form">
      <el-form-item label="Image" prop="url">
        <el-upload class="image-uploader" :action="state.uploadImgServer" accept="jpg,jpeg,png" :headers="{
    token: state.token
  }" :show-file-list="false" :before-upload="handleBeforeUpload" :on-success="handleUrlSuccess">
          <img v-if="state.ruleForm.url" :src="state.ruleForm.url" class="uploaded-image">
          <el-icon v-else class="upload-icon">
            <Plus />
          </el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="Redirect Link" prop="link">
        <el-input type="text" v-model="state.ruleForm.link"></el-input>
      </el-form-item>
      <el-form-item label="Sort Order" prop="sort">
        <el-input type="number" v-model="state.ruleForm.sort"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="state.visible = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Confirm</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue'
import axios from '@/utils/axios'
import { localGet, uploadImgServer } from '@/utils'
import { ElMessage } from 'element-plus'

const props = defineProps({
  type: String,
  reload: Function
})

const formRef = ref(null)
const state = reactive({
  uploadImgServer,
  token: localGet('token') || '',
  visible: false,
  ruleForm: {
    url: '',
    link: '',
    sort: ''
  },
  rules: {
    url: [
      { required: true, message: 'Image cannot be empty', trigger: ['change'] }
    ],
    sort: [
      { required: true, message: 'Sort order cannot be empty', trigger: ['change'] }
    ]
  },
  id: ''
})

// Fetch details
const getDetail = (id) => {
  axios.get(`/carousels/admin/detail/${id}`).then(res => {
    state.ruleForm = {
      url: res.carouselUrl,
      link: res.redirectUrl,
      sort: res.carouselRank
    }
  })
}

const handleBeforeUpload = (file) => {
  const suffix = file.name.split('.').pop() || ''
  if (!['jpg', 'jpeg', 'png'].includes(suffix)) {
    ElMessage.error('Please upload images in jpg, jpeg, png format')
    return false
  }
}

// Handle image upload success
const handleUrlSuccess = (val) => {
  state.ruleForm.url = val.data || ''
}

// Open dialog
const open = (id) => {
  state.visible = true
  if (id) {
    state.id = id
    getDetail(id)
  } else {
    state.ruleForm = {
      url: '',
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
      if (props.type === 'add') {
        axios.post('/carousels/admin/add', {
          carouselUrl: state.ruleForm.url,
          redirectUrl: state.ruleForm.link,
          carouselRank: state.ruleForm.sort
        }).then(() => {
          ElMessage.success('Added successfully')
          state.visible = false
          if (props.reload) props.reload()
        })
      } else {
        axios.put('/carousels/admin/update', {
          carouselId: state.id,
          carouselUrl: state.ruleForm.url,
          redirectUrl: state.ruleForm.link,
          carouselRank: state.ruleForm.sort
        }).then(() => {
          ElMessage.success('Updated successfully')
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
.image-uploader {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px dashed #d3d3d3;
  padding: 20px;
}

.uploaded-image {
  width: 200px;
  height: 100px;
  border: 1px solid #e9e9e9;
}

.upload-icon {
  font-size: 40px;
  color: #aaa;
}

.custom-form {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
}
</style>

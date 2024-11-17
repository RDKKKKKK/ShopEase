<template>
  <el-dialog :title="state.type === 'add' ? 'Add Category' : 'Edit Category'" v-model="state.visible" width="400px"
    :close-on-click-modal="false" :before-close="close">
    <el-form :model="state.ruleForm" :rules="state.rules" ref="formRef" label-width="120px" class="category-form">
      <el-form-item label="Name" prop="name">
        <el-input type="text" v-model="state.ruleForm.name" placeholder="Enter category name"></el-input>
      </el-form-item>
      <el-form-item label="Rank Value" prop="rank">
        <el-input type="number" v-model="state.ruleForm.rank" placeholder="Enter rank value"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="close" type="default">Cancel</el-button>
        <el-button type="primary" @click="submitForm">Confirm</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'

const props = defineProps({
  type: String, // Determines if it is add or edit mode
  reload: Function // Function to refresh the list after adding or editing
})

const formRef = ref(null)
const route = useRoute()
const state = reactive({
  visible: false,
  categoryLevel: 1,
  parentId: 0,
  ruleForm: {
    name: '',
    rank: ''
  },
  rules: {
    name: [
      { required: true, message: 'Name cannot be empty', trigger: 'change' }
    ],
    rank: [
      { required: true, message: 'Rank cannot be empty', trigger: 'change' }
    ]
  },
  id: ''
})

// Get details for editing
const getDetail = (id) => {
  axios.get(`/categories/admin/detail/${id}`).then(res => {
    state.ruleForm = {
      name: res.categoryName,
      rank: res.categoryRank
    }
    state.parentId = res.parentId
    state.categoryLevel = res.categoryLevel
  })
}



// Open dialog
const open = (id) => {
  if (id) {
    state.id = id
    getDetail(id) // Fetch details for editing
    state.type = 'edit'
  } else {
    // For adding a new category, set defaults from route
    state.type = 'add'
    const { level = 1, parent_id = 0 } = route.query
    state.ruleForm = {
      name: '',
      rank: ''
    }
    state.parentId = parent_id
    state.categoryLevel = level
  }
  state.visible = true
}

// Close dialog
const close = () => {
  state.visible = false
  formRef.value.resetFields()
}

// Submit form
const submitForm = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (props.type === 'add') {
        // Add category
        axios.post('/categories/admin/add', {
          categoryLevel: state.categoryLevel,
          parentId: state.parentId,
          categoryName: state.ruleForm.name,
          categoryRank: state.ruleForm.rank
        }).then(() => {
          ElMessage.success('Added successfully')
          state.visible = false
          if (props.reload) props.reload()
        })
      } else {
        // Edit category
        axios.put('/categories/admin/update', {
          categoryId: state.id,
          categoryLevel: state.categoryLevel,
          parentId: state.categoryLevel,
          categoryName: state.ruleForm.name,
          categoryRank: state.ruleForm.rank
        }).then(() => {
          ElMessage.success('Edited successfully')
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
.category-form {
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px 20px;
}

.el-dialog__header {
  background-color: #f2f2f2;
  border-bottom: 1px solid #eaeaea;
}

.el-form-item {
  margin-bottom: 20px;
}
</style>

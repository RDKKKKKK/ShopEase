<template>
  <el-card class="guest-container">
    <template #header>
      <div class="header">
        <el-button type="success" @click="handleSolve">Enable</el-button>
        <el-button type="danger" @click="handleForbid">Disable</el-button>
      </div>
    </template>
    <Table action='/users/admin/users' ref="table">
      <template #column>
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column prop="nickName" label="Nickname">
        </el-table-column>
        <el-table-column prop="loginName" label="Login Name">
        </el-table-column>
        <el-table-column label="Account Status">
          <template #default="scope">
            <span :style="scope.row.lockedFlag == 0 ? 'color: green;' : 'color: red;'">
              {{ scope.row.lockedFlag == 0 ? 'Active' : 'Disabled' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="Is Deleted">
          <template #default="scope">
            <span :style="scope.row.isDeleted == 0 ? 'color: green;' : 'color: red;'">
              {{ scope.row.isDeleted == 0 ? 'Active' : 'Deleted' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Registration Time">
        </el-table-column>
      </template>
    </Table>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import Table from '@/components/Table.vue'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'

let table = ref(null)
const handleSolve = () => {
  if (!table.value.state.multipleSelection.length) {
    ElMessage.error('Please select an item')
    return
  }
  const enable = 0
  axios.put(`/users/admin/users/${enable}`, table.value.state.multipleSelection.map(item => item.userId)  // 请求体中传递 ids 数组
  ).then(() => {
    ElMessage.success('Enable successful')
    table.value.getList()
  })
}
const handleForbid = () => {
  if (!table.value.state.multipleSelection.length) {
    ElMessage.error('Please select an item')
    return
  }
  const disable = 1
  axios.put(`/users/admin/users/${disable}`,
    table.value.state.multipleSelection.map(item => item.userId)  // 请求体中传递 ids 数组
  ).then(() => {
    ElMessage.success('Disable successful')
    table.value.getList()
  })
}
</script>

<style></style>

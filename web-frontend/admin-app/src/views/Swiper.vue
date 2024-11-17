<template>
  <el-card class="swiper-container">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="handleAdd">Add</el-button>
        <el-popconfirm title="Are you sure you want to delete?" confirmButtonText="Confirm" cancelButtonText="Cancel"
          @confirm="handleDelete">
          <template #reference>
            <el-button type="danger" :icon="Delete">Batch Delete</el-button>
          </template>
        </el-popconfirm>
      </div>
    </template>
    <el-table :loading="state.loading" ref="multipleTable" :data="state.tableData" tooltip-effect="dark"
      style="width: 100%; border-radius: 8px; overflow: hidden;" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column label="Carousel Image" width="200">
        <template #default="scope">
          <img style="width: 150px; height: 150px; border-radius: 5px;" :src="scope.row.carouselUrl"
            alt="Carousel Image">
        </template>
      </el-table-column>
      <el-table-column label="Redirect Link">
        <template #default="scope">
          <a target="_blank" :href="scope.row.redirectUrl">{{ scope.row.redirectUrl }}</a>
        </template>
      </el-table-column>
      <el-table-column prop="carouselRank" label="Order Value" width="120">
      </el-table-column>
      <el-table-column prop="createTime" label="Date Added" width="200">
      </el-table-column>
      <el-table-column label="Actions" width="220">
        <template #default="scope">
          <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row.carouselId)">Edit</a>
          <el-popconfirm title="Are you sure you want to delete?" confirmButtonText='Confirm' cancelButtonText='Cancel'
            @confirm="handleDeleteOne(scope.row.categoryId)">
            <template #reference>
              <a style="cursor: pointer">Delete</a>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" :total="state.total" :page-size="state.pageSize"
      :current-page="state.currentPage" @current-change="changePage" style="margin-top: 10px;" />
  </el-card>
  <DialogAddSwiper ref='addSwiper' :reload="getCarousels" :type="state.type" />
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogAddSwiper from '@/components/DialogAddSwiper.vue'

const addSwiper = ref()
const state = reactive({
  loading: false,
  tableData: [], // Data list
  currentPage: 1,
  pageSize: 10,
  type: 'add', // Operation type
  multipleSelection: [], // Selected items
  total: 0, // Total count
})

onMounted(() => {
  getCarousels()
})

// Get carousel list
const getCarousels = () => {
  state.loading = true
  axios.get('/carousels/admin/list', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize
    }
  }).then(res => {
    state.tableData = res.list
    state.total = res.totalCount
    state.currentPage = res.currPage
    state.loading = false
  })
}

// Add carousel item
const handleAdd = () => {
  state.type = 'add'
  addSwiper.value.open()
}

// Edit carousel image
const handleEdit = (id) => {
  state.type = 'edit'
  addSwiper.value.open(id)
}

// Selection change method, triggered when selection changes
const handleSelectionChange = (val) => {
  state.multipleSelection = val
}

// Batch delete
const handleDelete = () => {
  if (!state.multipleSelection.length) {
    ElMessage.error('Please select items')
    return
  }
  axios.delete('/carousels/admin/batchDelete', {
    data: {
      ids: state.multipleSelection.map(i => i.carouselId)
    }
  }).then(() => {
    ElMessage.success('Deletion successful')
    getCarousels()
  })
}
const handleDeleteOne = (id) => {
  axios.delete('/carousels/admin/batchDelete', {
    data: {
      ids: [id]
    }
  }).then(() => {
    ElMessage.success('Delete successful')
    getCategory()
  })
}

// Change page
const changePage = (val) => {
  state.currentPage = val
  getCarousels()
}
</script>

<style>
.header {
  display: flex;
  justify-content: space-between;
  /* padding: 10px; */
  /* background-color: #f9f9f9; */
  /* border-bottom: 1px solid #eaeaea; */
}

.swiper-container {
  min-height: 100%;
  padding: 20px;
  /* margin: 20px; */
  border: 1px solid #eaeaea;
  /* border-radius: 8px; */
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.el-table {
  border: none;
}

.el-table th,
.el-table td {
  background-color: #fff;
  color: #333;
}

.el-pagination {
  padding: 10px 0;
}
</style>

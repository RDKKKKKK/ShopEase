<template>
  <el-card class="category-container">
    <template #header>
      <div class="header">
        <el-button type="primary" :icon="Plus" @click="handleAdd">Add</el-button>
        <el-popconfirm title="Are you sure you want to delete?" confirmButtonText='Confirm' cancelButtonText='Cancel'
          @confirm="handleDelete">
          <template #reference>
            <el-button type="danger" :icon="Delete">Batch Delete</el-button>
          </template>
        </el-popconfirm>
      </div>
    </template>
    <el-table :load="state.loading" ref="multipleTable" :data="state.tableData" tooltip-effect="dark"
      style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="categoryName" label="Category Name">
      </el-table-column>
      <el-table-column prop="categoryRank" label="Sort Value" width="120">
      </el-table-column>
      <el-table-column prop="createTime" label="Date Added" width="200">
      </el-table-column>
      <el-table-column label="Actions" width="220">
        <template #default="scope">
          <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row.categoryId)">Edit</a>
          <a style="cursor: pointer; margin-right: 10px" @click="handleNext(scope.row)">Subcategories</a>
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
      :current-page="state.currentPage" @current-change="changePage" />
    <DialogAddCategory ref='addCate' :reload="getCategory" :type="state.type" />
  </el-card>
</template>

<script setup>
import { onMounted, onUnmounted, reactive, ref, toRefs, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'
import DialogAddCategory from '@/components/DialogAddCategory.vue'

const addCate = ref(null)
const router = useRouter()
const route = useRoute()
const state = reactive({
  loading: false,
  tableData: [],
  multipleSelection: [],
  total: 0,
  currentPage: 1,
  pageSize: 10,
  type: '',
  level: 1,
  parent_id: 0
})

onMounted(() => {
  getCategory()
})

const unwatch = router.afterEach((to) => {
  if (['category', 'level2', 'level3'].includes(to.name)) {
    getCategory()
  }
})

onUnmounted(() => {
  unwatch()
})

const getCategory = () => {
  const { level = 1, parent_id = 0 } = route.query
  state.loading = true
  axios.get('/categories/admin/list', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
      categoryLevel: level,
      parentId: parent_id
    }
  }).then(res => {
    state.tableData = res.list
    state.total = res.totalCount
    state.currentPage = res.currPage
    state.loading = false
    state.level = level
    state.parentId = parent_id
  })
}

const changePage = (val) => {
  state.currentPage = val
  getCategory()
}

const handleNext = (item) => {
  const levelNumber = item.categoryLevel + 1
  if (levelNumber == 4) {
    ElMessage.error('No next level available')
    return
  }
  router.push({
    name: `level${levelNumber}`,
    query: {
      level: levelNumber,
      parent_id: item.categoryId
    }
  })
}

const handleAdd = () => {
  state.type = 'add'
  addCate.value.open()
}

const handleEdit = (id) => {
  state.type = 'edit'
  addCate.value.open(id)
}

const handleSelectionChange = (val) => {
  state.multipleSelection = val
}

const handleDelete = () => {
  if (!state.multipleSelection.length) {
    ElMessage.error('Please select items')
    return
  }
  axios.delete('/categories/admin/batchDelete', {
    data: {
      ids: state.multipleSelection.map(i => i.categoryId)
    }
  }).then(() => {
    ElMessage.success('Delete successful')
    getCategory()
  })
}

const handleDeleteOne = (id) => {
  axios.delete('/categories/admin/batchDelete', {
    data: {
      ids: [id]
    }
  }).then(() => {
    ElMessage.success('Delete successful')
    getCategory()
  })
}
</script>

<style>
.category-container {
  background-color: #f5f5f5;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.el-table {
  border-radius: 8px;
  overflow: hidden;
}

.el-table th {
  background-color: #409eff;
  color: #000000;
}

.el-table td {
  background-color: #ffffff;
}

.el-pagination {
  margin-top: 15px;
}
</style>

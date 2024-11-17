<template>
  <el-card class="index-container">
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
      <el-table-column prop="goodsId" label="Product ID" width="200">
      </el-table-column>
      <el-table-column prop="configName" label="Product Name">
      </el-table-column>
      <el-table-column label="Redirect Link">
        <template #default="scope">
          <a target="_blank" :href="scope.row.redirectUrl">{{ scope.row.redirectUrl }}</a>
        </template>
      </el-table-column>
      <el-table-column prop="configRank" label="Sort Value" width="120">
      </el-table-column>
      <el-table-column prop="createTime" label="Added Time" width="200">
      </el-table-column>
      <el-table-column label="Actions" width="120">
        <template #default="scope">
          <a style="cursor: pointer; margin-right: 10px" @click="handleEdit(scope.row.configId)">Edit</a>
          <el-popconfirm title="Are you sure you want to delete?" confirmButtonText='Confirm' cancelButtonText='Cancel'
            @confirm="handleDeleteOne(scope.row.configId)">
            <template #reference>
              <a style="cursor: pointer">Delete</a>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- Show pagination if total exceeds one page -->
    <el-pagination background layout="prev, pager, next" :total="state.total" :page-size="state.pageSize"
      :current-page="state.currentPage" @current-change="changePage" />
  </el-card>
  <DialogAddGood ref='addGood' :reload="getIndexConfig" :type="state.type" :configType="state.configType" />
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import DialogAddGood from '@/components/DialogAddGood.vue'
import { useRoute, useRouter } from 'vue-router'
import axios from '@/utils/axios'

// Home page configuration type parameters
const configTypeMap = {
  hot: 3,
  new: 4,
  recommend: 5
}

const router = useRouter()
const route = useRoute()
const multipleTable = ref(null)
const addGood = ref(null)
const state = reactive({
  loading: false,
  tableData: [], // Data list
  multipleSelection: [], // Selected items
  total: 0, // Total count
  currentPage: 1, // Current page
  pageSize: 10, // Page size
  type: 'add', // Operation type
  configType: 3 // 3-(Homepage) Hot Products, 4-(Homepage) New Arrivals, 5-(Homepage) Recommended
})

// Watch for route changes
router.beforeEach((to) => {
  if (['hot', 'new', 'recommend'].includes(to.name)) {
    state.configType = configTypeMap[to.name]
    state.currentPage = 1
    getIndexConfig()
  }
})

// Initialize
onMounted(() => {
  state.configType = configTypeMap[route.name]
  getIndexConfig()
})

// Home page hot products list
const getIndexConfig = () => {
  state.loading = true
  axios.get('/indexConfigs/admin/list', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
      configType: state.configType
    }
  }).then(res => {
    state.tableData = res.list
    state.total = res.totalCount
    state.currentPage = res.currPage
    state.loading = false
  })
}

// Add product
const handleAdd = () => {
  state.type = 'add'
  addGood.value.open()
}

// Edit product
const handleEdit = (id) => {
  state.type = 'edit'
  addGood.value.open(id)
}

// Selected items
const handleSelectionChange = (val) => {
  state.multipleSelection = val
}

// Delete
const handleDelete = () => {
  if (!state.multipleSelection.length) {
    ElMessage.error('Please select items')
    return
  }
  axios.delete('/indexConfigs/admin/batchDelete', {
    ids: state.multipleSelection.map(i => i.configId)
  }).then(() => {
    ElMessage.success('Deletion successful')
    getIndexConfig()
  })
}

// Single delete
const handleDeleteOne = (id) => {
  axios.delete('/indexConfigs/admin/batchDelete', {
    ids: [id]
  }).then(() => {
    ElMessage.success('Deletion successful')
    getIndexConfig()
  })
}

const changePage = (val) => {
  state.currentPage = val
  getIndexConfig()
}
</script>

<style scoped>
.index-container {
  min-height: 100%;
  padding: 20px;
  /* Added padding for better spacing */
}

.header {
  display: flex;
  /* Flexbox for header layout */
  justify-content: space-between;
  /* Space between buttons */
  margin-bottom: 15px;
  /* Space below header */
}

.el-card.is-always-shadow {
  min-height: 100% !important;
  border-radius: 8px;
  /* Rounded corners */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  /* Softer shadow */
}

.el-table {
  border-radius: 8px;
  /* Rounded corners for table */
}

.el-pagination {
  margin-top: 20px;
  /* Space above pagination */
}
</style>

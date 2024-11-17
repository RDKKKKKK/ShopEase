<template>
  <el-card class="order-container">
    <template #header>
      <div class="header">
        <el-input style="width: 200px; margin-right: 10px" placeholder="Please enter order number"
          v-model="state.orderNo" @change="handleOption" clearable />
        <el-select @change="handleOption" v-model="state.orderStatus" style="width: 200px; margin-right: 10px">
          <el-option v-for="item in state.options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-button type="primary" :icon="HomeFilled" @click="handleConfig()">Complete Picking</el-button>
        <el-button type="primary" :icon="HomeFilled" @click="handleSend()">Outbound</el-button>
        <el-button type="danger" :icon="Delete" @click="handleClose()">Close Order</el-button>
      </div>
    </template>
    <el-table :load="state.loading" :data="state.tableData" tooltip-effect="dark" style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="orderNo" label="Order Number" width="180">
      </el-table-column>
      <el-table-column prop="totalPrice" label="Total Order Price" width="150">
      </el-table-column>
      <el-table-column prop="orderStatus" label="Order Status" width="150">
        <template #default="scope">
          <span>{{ $filters.orderMap(scope.row.orderStatus) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="payType" label="Payment Method" width="120">
        <template #default='scope'>
          <span v-if="scope.row.payType == 1">WeChat Pay</span>
          <span v-else-if="scope.row.payType == 2">Alipay</span>
          <span v-else>Unknown</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="Creation Time" width="180">
      </el-table-column>
      <el-table-column label="Actions" align="center">
        <template #default="scope">
          <el-popconfirm v-if="scope.row.orderStatus == 1" title="Are you sure to complete the picking?"
            @confirm="handleConfig(scope.row.orderId)" confirm-button-text="Yes" cancel-button-text="No">
            <template #reference>
              <a style="cursor: pointer; margin-right: 10px">Complete Picking</a>
            </template>
          </el-popconfirm>
          <el-popconfirm v-if="scope.row.orderStatus == 2" title="Are you sure to outbound?"
            @confirm="handleSend(scope.row.orderId)" confirm-button-text="Yes" cancel-button-text="No">
            <template #reference>
              <a style="cursor: pointer; margin-right: 10px">Outbound</a>
            </template>
          </el-popconfirm>
          <el-popconfirm v-if="!(scope.row.orderStatus == 4 || scope.row.orderStatus < 0)"
            title="Are you sure to close the order?" @confirm="handleClose(scope.row.orderId)" confirm-button-text="Yes"
            cancel-button-text="No">
            <template #reference>
              <a style="cursor: pointer; margin-right: 10px">Close Order</a>
            </template>
          </el-popconfirm>
          <router-link :to="{ path: '/order_detail', query: { id: scope.row.orderId } }">Order Details</router-link>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" :total="state.total" :page-size="state.pageSize"
      :current-page="state.currentPage" @current-change="changePage" />
  </el-card>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { HomeFilled, Delete } from '@element-plus/icons-vue'
import axios from '@/utils/axios'

const state = reactive({
  loading: false,
  tableData: [], // Data list
  multipleSelection: [], // Selected items
  total: 0, // Total number of items
  currentPage: 1, // Current page
  pageSize: 10, // Page size
  orderNo: '', // Order number
  orderStatus: '', // Order status
  // Default values for order status filter options
  options: [{
    value: '',
    label: 'All'
  }, {
    value: 0,
    label: 'Pending Payment'
  }, {
    value: 1,
    label: 'Paid'
  }, {
    value: 2,
    label: 'Picking Completed'
  }, {
    value: 3,
    label: 'Outbound Successful'
  }, {
    value: 4,
    label: 'Transaction Successful'
  }, {
    value: -1,
    label: 'Manually Closed'
  }, {
    value: -2,
    label: 'Timeout Closed'
  }, {
    value: -3,
    label: 'Merchant Closed'
  }]
})
// Initialize to get order list
onMounted(() => {
  getOrderList()
})
// Method to get the order list
const getOrderList = () => {
  state.loading = true
  axios.get('/orders/admin/list', {
    params: {
      pageNumber: state.currentPage,
      pageSize: state.pageSize,
      orderNo: state.orderNo,
      orderStatus: state.orderStatus
    }
  }).then(res => {
    state.tableData = res.list
    state.total = res.totalCount
    state.currentPage = res.currPage
    state.loading = false
  })
}
// Trigger filter options method
const handleOption = () => {
  state.currentPage = 1
  getOrderList()
}
// Checkbox selection
const handleSelectionChange = (val) => {
  state.multipleSelection = val
}
// Pagination method
const changePage = (val) => {
  state.currentPage = val
  getOrderList()
}
// Picking method
const handleConfig = (id) => {
  let params
  // Single configuration
  if (id) {
    params = [id]
  } else {
    if (!state.multipleSelection.length) {
      console.log('state.multipleSelection', state.multipleSelection.length)
      ElMessage.error('Please select items')
      return
    }
    // Multiple selection configuration
    params = state.multipleSelection.map(i => i.orderId)
  }
  axios.put('/orders/admin/checkDone', {
    ids: params
  }).then(() => {
    ElMessage.success('Picking successful')
    getOrderList()
  })
}
// Outbound method
const handleSend = (id) => {
  let params
  if (id) {
    params = [id]
  } else {
    if (!state.multipleSelection.length) {
      ElMessage.error('Please select items')
      return
    }
    params = state.multipleSelection.map(i => i.orderId)
  }
  axios.put('/orders/admin/checkOut', {
    ids: params
  }).then(() => {
    ElMessage.success('Outbound successful')
    getOrderList()
  })
}
// Close order method
const handleClose = (id) => {
  let params
  if (id) {
    params = [id]
  } else {
    if (!state.multipleSelection.length) {
      ElMessage.error('Please select items')
      return
    }
    params = state.multipleSelection.map(i => i.orderId)
  }
  axios.put('/orders/admin/close', {
    ids: params
  }).then(() => {
    ElMessage.success('Close successful')
    getOrderList()
  })
}
</script>

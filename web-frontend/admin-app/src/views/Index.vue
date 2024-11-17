<template>
  <el-card class="introduce">
    <div class="order">
      <el-card class="order-item">
        <template #header>
          <div class="card-header">
            <span>Today's Order Count</span>
          </div>
        </template>
        <div class="item">1888</div>
      </el-card>
      <el-card class="order-item">
        <template #header>
          <div class="card-header">
            <span>Active Users Today</span>
          </div>
        </template>
        <div class="item">36271</div>
      </el-card>
      <el-card class="order-item">
        <template #header>
          <div class="card-header">
            <span>Conversion Rate</span>
          </div>
        </template>
        <div class="item">20%</div>
      </el-card>
    </div>
    <div id="zoom"></div>
  </el-card>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'

let myChart = null

onMounted(() => {
  if (window.echarts) {
    // Initialize echarts instance based on the prepared DOM
    myChart = window.echarts.init(document.getElementById('zoom'))

    // Specify chart configuration and data
    const option = {
      title: {
        text: 'System Line Chart'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['New Registrations', 'Paying Users', 'Active Users', 'Order Count', 'Total Revenue Today']
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          boundaryGap: false,
          data: ['2021-03-11', '2021-03-12', '2021-03-13', '2021-03-14', '2021-03-15', '2021-03-16', '2021-03-17']
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          name: 'New Registrations',
          type: 'line',
          stack: 'Total',
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
          name: 'Paying Users',
          type: 'line',
          stack: 'Total',
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
          name: 'Active Users',
          type: 'line',
          stack: 'Total',
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          data: [150, 232, 201, 154, 190, 330, 410]
        },
        {
          name: 'Order Count',
          type: 'line',
          stack: 'Total',
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          data: [320, 332, 301, 334, 390, 330, 320]
        },
        {
          name: 'Total Revenue Today',
          type: 'line',
          stack: 'Total',
          label: {
            show: true,
            position: 'top'
          },
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          data: [820, 932, 901, 934, 1290, 1330, 1320]
        }
      ]
    }

    // Use the specified configuration and data to display the chart.
    myChart.setOption(option)
  }
})

onUnmounted(() => {
  myChart.dispose()
})
</script>

<style scoped>
.introduce {
  padding: 20px;
  background-color: #f9f9f9;
}

.order {
  display: flex;
  margin-bottom: 50px;
  gap: 20px; /* Added gap for better spacing */
}

.order-item {
  flex: 1;
  background-color: #ffffff; /* White background for cards */
  border: 1px solid #e4e4e4; /* Light border for card */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
}

.card-header {
  font-weight: bold;
  font-size: 16px; /* Increased font size */
  color: #333; /* Darker text color */
  padding: 10px; /* Padding for better spacing */
}

.item {
  font-size: 24px; /* Larger font for values */
  text-align: center; /* Center align the item */
  padding: 20px; /* Padding for the item */
}

#zoom {
  min-height: 300px;
  margin-top: 20px; /* Margin for spacing from cards */
}
</style>

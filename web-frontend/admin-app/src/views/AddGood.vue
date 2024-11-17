<template>
  <div class="add-page">
    <el-card class="add-container">
      <el-form :model="state.productForm" :rules="state.rules" ref="productRef" label-width="120px" class="productForm">
        <el-form-item required label="Category">
          <el-cascader v-model="state.productForm.goodsCategoryId" :placeholder="state.defaultCate" style="width: 300px"
            :props="state.category" @change="handleChangeCate"></el-cascader>
        </el-form-item>

        <el-form-item label="Product Name" prop="goodsName">
          <el-input style="width: 350px" v-model="state.productForm.goodsName"
            placeholder="Enter product name"></el-input>
        </el-form-item>

        <el-form-item label="Description" prop="goodsIntro">
          <el-input type="textarea" style="width: 350px" v-model="state.productForm.goodsIntro"
            placeholder="Enter description (100 characters max)"></el-input>
        </el-form-item>

        <el-form-item label="Original Price" prop="originalPrice">
          <el-input type="number" min="0" style="width: 350px" v-model="state.productForm.originalPrice"
            placeholder="Enter price"></el-input>
        </el-form-item>

        <el-form-item label="Selling Price" prop="sellingPrice">
          <el-input type="number" min="0" style="width: 350px" v-model="state.productForm.sellingPrice"
            placeholder="Enter selling price"></el-input>
        </el-form-item>

        <el-form-item label="Stock" prop="stockNum">
          <el-input type="number" min="0" style="width: 350px" v-model="state.productForm.stockNum"
            placeholder="Enter stock quantity"></el-input>
        </el-form-item>

        <el-form-item label="Tags" prop="tag">
          <el-input style="width: 350px" v-model="state.productForm.tag" placeholder="Enter product tags"></el-input>
        </el-form-item>

        <el-form-item label="Status" prop="goodsSellStatus">
          <el-radio-group v-model="state.productForm.goodsSellStatus">
            <el-radio :label="0">Available</el-radio>
            <el-radio :label="1">Unavailable</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Cover Image" required prop="goodsCoverImg">
          <el-upload class="image-uploader" :action="state.uploadUrl" accept=".jpg,.jpeg,.png"
            :headers="{ token: state.token }" :show-file-list="false" :before-upload="validateImage"
            :on-success="uploadSuccess">
            <img v-if="state.productForm.goodsCoverImg" :src="state.productForm.goodsCoverImg"
              class="product-image-preview">
            <el-icon v-else class="upload-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="Details">
          <div ref="editor" class="editor-container"></div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">
            {{ state.productId ? 'Update Product' : 'Create Product' }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeUnmount, getCurrentInstance } from 'vue'
import WangEditor from 'wangeditor'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { localGet, uploadImgServer, uploadImgsServer } from '@/utils'
import i18next from 'i18next';

const { proxy } = getCurrentInstance()
const editor = ref(null)
const productRef = ref(null)
const route = useRoute()
const router = useRouter()
const { id } = route.query

const state = reactive({
  uploadUrl: uploadImgServer,
  token: localGet('token') || '',
  productId: id,
  defaultCategory: '',
  productForm: {
    goodsCategoryId: 0,
    goodsName: '',
    goodsIntro: '',
    originalPrice: '',
    sellingPrice: '',
    stockNum: '',
    goodsSellStatus: '0',
    goodsCoverImg: '',
    tag: ''
  },
  rules: {
    goodsName: [{ required: true, message: 'Please enter product name', trigger: 'blur' }],
    originalPrice: [{ required: true, message: 'Please enter price', trigger: 'blur' }],
    sellingPrice: [{ required: true, message: 'Please enter selling price', trigger: 'blur' }],
    stockNum: [{ required: true, message: 'Please enter stock quantity', trigger: 'blur' }],
  },
  categoryId: '',
  category: {
    lazy: true,
    lazyLoad(node, resolve) {
      const { level = 0, value } = node
      axios.get('/categories/admin/list', {
        params: {
          pageNumber: 1,
          pageSize: 1000,
          categoryLevel: level + 1,
          parentId: value || 0
        }
      }).then(res => {
        const list = res.list
        const nodes = list.map(item => ({
          value: item.categoryId,
          label: item.categoryName,
          leaf: level > 1
        }))
        resolve(nodes)
      })
    }
  }
})

let editorInstance
onMounted(() => {
  editorInstance = new WangEditor(editor.value)
  // 选择语言
  editorInstance.config.lang = 'en'
  // 自定义语言
  editorInstance.config.languages['en'] = {
    wangEditor: {
      插入: 'insert',
      默认: 'default',
      创建: 'create',
      修改: 'edit',
      如: 'like',
      请输入正文: 'please enter the text',
      menus: {
        title: {
          标题: 'Title',
          加粗: 'Bold',
          字号: 'Font',
          字体: 'Typefave',
          斜体: 'Italic',
          下划线: 'Underline',
          删除线: 'Delete the line',
          缩进: 'Indentation',
          行高: 'Lineheight',
          文字颜色: 'Font-color',
          背景色: 'Cackground-color',
          链接: 'Link',
          序列: 'Sequnce',
          对齐: 'Align',
          引用: 'Quote',
          表情: 'Expressions',
          图片: 'Images',
          视频: 'Videos',
          表格: 'Forms',
          代码: 'Code',
          分割线: 'Divided Line',
          恢复: 'Recover',
          撤销: 'Revoke',
          全屏: 'Full Screen',
          待办事项: 'To-Do'
        },
        dropListMenu: {
          设置标题: 'title',
          背景颜色: 'background',
          文字颜色: 'font color',
          设置字号: 'font size',
          设置字体: 'font family',
          设置缩进: 'indent',
          对齐方式: 'align',
          设置行高: 'line heihgt',
          序列: 'list',
          head: {
            正文: 'text',
          },
          indent: {
            增加缩进: 'indent',
            减少缩进: 'outdent',
          },
          justify: {
            靠左: 'left',
            居中: 'center',
            靠右: 'right',
          },
          list: {
            无序列表: 'unordered',
            有序列表: 'ordered',
          },
        },
        panelMenus: {
          emoticon: {
            默认: 'default',
            新浪: 'sina',
            emoji: 'emoji',
            手势: 'gesture',
          },
          image: {
            图片地址: 'Url',
            上传图片: 'upload image',
            网络图片: 'network image',
            图片文字说明: 'Alt',
            跳转链接: 'Link'
          },
          link: {
            链接: 'link',
            链接文字: 'link text',
            删除链接: 'delete',
            查看链接: 'view',
          },
          video: {
            插入视频: 'insert video',
          },
          table: {
            行: 'row',
            列: 'column',
            的: ' ',
            表格: 'table',
            添加行: 'add row',
            删除行: 'delete row',
            添加列: 'add column',
            删除列: 'delete column',
            设置表头: 'set header',
            取消表头: 'cancel header',
            插入表格: 'insert table',
            删除表格: 'delete table',
          },
          code: {
            删除代码: 'delete code',
            修改代码: 'edit code',
            插入代码: 'insert code',
          },
        },
      },
      validate: {
        张图片: 'images',
        大于: 'greater than',
        图片链接: 'image link',
        不是图片: 'is not image',
        返回结果: 'return results',
        上传图片超时: 'upload image timeout',
        上传图片错误: 'upload image error',
        上传图片失败: 'upload image failed',
        插入图片错误: 'insert image error',
        一次最多上传: 'once most at upload',
        下载链接失败: 'download link failed',
        图片验证未通过: 'image validate failed',
        服务器返回状态: 'server return status',
        上传图片返回结果错误: 'upload image return results error',
        请替换为支持的图片类型: 'please replace with a supported image type',
        您插入的网络图片无法识别: 'the network picture you inserted is not recognized',
        您刚才插入的图片链接未通过编辑器校验: 'the image link you just inserted did not pass the editor verification',
      },
    }
  }
  // 引入 i18next 插件
  editorInstance.i18next = i18next
  editorInstance.config.uploadImgServer = uploadImgsServer
  editorInstance.config.uploadImgMaxSize = 2 * 1024 * 1024 // 2MB、editorInstance.config.lang = 'zh-CN'  // 中文


  editorInstance.create()

  if (id) {
    axios.get(`/goods/admin/detail/${id}`).then((response) => {
      Object.assign(state.productForm, response.goods)
      // state.categoryId = response.goods.goodsCategoryId
      state.categoryId = [1, 5, 33]
      editorInstance.txt.html(response.goods.goodsDetailContent)
    })
  }
})

onBeforeUnmount(() => {
  editorInstance.destroy()
})

const handleSubmit = () => {
  productRef.value.validate(valid => {
    if (valid) {
      const params = { ...state.productForm, goodsDetailContent: editorInstance.txt.html() }
      if (state.productId) {
        axios.put('/goods/admin/update', params).then(() => {
          ElMessage.success('Product updated successfully')
          resetForm()
        })
      } else {
        params.goodsCategoryId = params.goodsCategoryId[params.goodsCategoryId.length - 1]
        // params.goodsCoverImg = 'https://shopease-mall.oss-cn-beijing.aliyuncs.com/images/freebuds-pro.png'
        axios.post('/goods/admin/add', params).then(() => {
          ElMessage.success('Product created successfully')
          resetForm()
        })
      }
      router.push({ path: '/add' })
    }
  })
}

// 清空表单的函数
const resetForm = () => {
  state.productForm = {
    goodsCategoryId: 0,
    goodsName: '',
    goodsIntro: '',
    originalPrice: '',
    sellingPrice: '',
    stockNum: '',
    goodsSellStatus: '0',
    goodsCoverImg: '',
    tag: ''
  };
  editorInstance.txt.html(''); // 清空编辑器内容
};

const validateImage = file => {
  const isValidFormat = ['jpg', 'jpeg', 'png'].includes(file.name.split('.').pop().toLowerCase())
  if (!isValidFormat) ElMessage.error('Please upload an image in jpg, jpeg, or png format')
  return isValidFormat
}

const uploadSuccess = (response) => {
  state.productForm.goodsCoverImg = response.data
}
const handleChangeCate = (val) => {
  state.categoryId = val[2] || 0
}
</script>

<style scoped>
.add-page {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background-color: #f5f5f5;
}

.add-container {
  width: 600px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background-color: white;
}

.image-uploader {
  width: 120px;
  height: 120px;
}

.product-image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.upload-icon {
  font-size: 40px;
  color: #ccc;
}

.editor-container {
  border: 1px solid #ddd;
  padding: 10px;
  min-height: 150px;
}
</style>

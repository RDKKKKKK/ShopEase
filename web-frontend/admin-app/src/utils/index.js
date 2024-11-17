export function localGet (key) {
  const value = window.localStorage.getItem(key)
  try {
    return JSON.parse(window.localStorage.getItem(key))
  } catch (error) {
    return value
  }
}

export function localSet (key, value) {
  window.localStorage.setItem(key, JSON.stringify(value))
}

export function localRemove (key) {
  window.localStorage.removeItem(key)
}

// 判断内容是否含有表情字符，现有数据库不支持。
export function hasEmoji (str = '') {
  const reg = /[^\u0020-\u007E\u00A0-\u00BE\u2E80-\uA4CF\uF900-\uFAFF\uFE30-\uFE4F\uFF00-\uFFEF\u0080-\u009F\u2000-\u201f\u2026\u2022\u20ac\r\n]/g;
  return str.match(reg) && str.match(reg).length
}

// 单张图片上传
export const uploadImgServer = 'http://localhost:29010/goods/admin/upload/file'
// 多张图片上传
export const uploadImgsServer = 'http://localhost:29010/goods/admin/upload/files'

export const pathMap = {
  login: 'Login',
  introduce: 'Introduction',
  dashboard: 'Dashboard',
  add: 'Add A Product',
  swiper: 'Swiper Product',
  hot: 'Hot Product',
  new: 'New Product',
  recommend: 'Recommend',
  category: 'Category',
  level2: 'level2',
  level3: 'level3',
  good: 'Good',
  guest: 'Member',
  order: 'Order',
  order_detail: 'Order_detail',
  account: 'Account'
}
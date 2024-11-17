
import axios from '../utils/axios'

export function getDetail(id) {
  return axios.get(`/goods/mall/detail/${id}`);
}

export function getCategory() {
  return axios.get('/categories/mall/listAll');
}

export function search(params) {
  return axios.get('/goods/mall/search', { params });
}


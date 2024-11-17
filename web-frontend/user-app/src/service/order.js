
import axios from '../utils/axios'

export function createOrder(params) {
  return axios.post('/orders/mall/saveOrder', params);
}

export function getOrderList(params) {
  return axios.get('/orders/mall/order', { params });
}

export function getOrderDetail(id) {
  return axios.get(`/orders/mall/order/${id}`);
}

export function cancelOrder(id) {
  return axios.put(`/orders/mall/order/${id}/cancel`);
}

export function confirmOrder(id) {
  return axios.put(`/orders/mall/order/${id}/finish`)
}

export function payOrder(params) {
  return axios.get('/orders/mall/paySuccess', { params })
}





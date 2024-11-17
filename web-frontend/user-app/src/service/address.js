
import axios from '../utils/axios'

export function addAddress(params) {
  return axios.post('/mall/address', params);
}

export function EditAddress(params) {
  return axios.put('/mall/address', params);
}

export function DeleteAddress(id) {
  return axios.delete(`/mall/address/${id}`);
}

export function getDefaultAddress() {
  return axios.get('/mall/address/default');
}

export function getAddressList() {
  return axios.get('/mall/address', { pageNumber: 1, pageSize: 1000 })
}

export function getAddressDetail(id) {
  return axios.get(`/mall/address/${id}`)
}



import axios from '../utils/axios'

export function getUserInfo() {
  return axios.get('/users/mall/detail');
}

export function EditUserInfo(params) {
  return axios.put('/users/mall/update', params);
}

export function login(params) {
  return axios.post('/users/mall/login', params);
}

export function logout() {
  return axios.post('/users/mall/logout')
}

export function register(params) {
  return axios.post('/users/mall/register', params);
}

import request from '@/utils/request'

export function listClass(query) {
  return request({
    url: '/class/list',
    method: 'get',
    params: query
  })
}

export function createClass(data) {
  return request({
    url: '/class/create',
    method: 'post',
    data
  })
}

export function readClass(data) {
  return request({
    url: '/class/read',
    method: 'get',
    data
  })
}

export function updateClass(data) {
  return request({
    url: '/class/update',
    method: 'post',
    data
  })
}

export function deleteClass(data) {
  return request({
    url: '/class/delete',
    method: 'post',
    data
  })
}

import request from '@/utils/request'

export function listTask(query) {
  return request({
    url: '/task/list',
    method: 'get',
    params: query
  })
}

export function createTask(data) {
  return request({
    url: '/task/create',
    method: 'post',
    data
  })
}

export function readTask(data) {
  return request({
    url: '/task/read',
    method: 'get',
    data
  })
}

export function updateTask(data) {
  return request({
    url: '/task/update',
    method: 'post',
    data
  })
}

export function deleteTask(data) {
  return request({
    url: '/task/delete',
    method: 'post',
    data
  })
}

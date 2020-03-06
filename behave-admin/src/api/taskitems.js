import request from '@/utils/request'

export function listTaskItems(query) {
  return request({
    url: '/taskitem/list',
    method: 'get',
    params: query
  })
}

export function createTaskItems(data) {
  return request({
    url: '/taskitem/create',
    method: 'post',
    data
  })
}

export function readTaskItems(data) {
  return request({
    url: '/taskitem/read',
    method: 'get',
    data
  })
}

export function updateTaskItems(data) {
  return request({
    url: '/taskitem/update',
    method: 'post',
    data
  })
}

export function deleteTaskItems(data) {
  return request({
    url: '/taskitem/delete',
    method: 'post',
    data
  })
}

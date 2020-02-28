import request from '@/utils/request'

export function listScheduleSolt(query) {
  return request({
    url: '/scheduleSolt/list',
    method: 'get',
    params: query
  })
}

export function createScheduleSolt(data) {
  return request({
    url: '/scheduleSolt/create',
    method: 'post',
    data
  })
}

export function readScheduleSolt(data) {
  return request({
    url: '/scheduleSolt/read',
    method: 'get',
    data
  })
}

export function updateScheduleSolt(data) {
  return request({
    url: '/scheduleSolt/update',
    method: 'post',
    data
  })
}

export function deleteScheduleSolt(data) {
  return request({
    url: '/scheduleSolt/delete',
    method: 'post',
    data
  })
}

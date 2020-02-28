import request from '@/utils/request'

export function listTeacher(query) {
  return request({
    url: '/user/listTeacher',
    method: 'get',
    params: query
  })
}

export function createTeacher(data) {
  return request({
    url: '/user/create',
    method: 'post',
    data
  })
}

export function readTeacher(data) {
  return request({
    url: '/user/read',
    method: 'get',
    data
  })
}

export function updateTeacher(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}

export function deleteTeacher(data) {
  return request({
    url: '/user/delete',
    method: 'post',
    data
  })
}

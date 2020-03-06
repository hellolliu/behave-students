import request from '@/utils/request'

export function listTeacher(query) {
  return request({
    url: '/teacher/list',
    method: 'get',
    params: query
  })
}

export function createTeacher(data) {
  return request({
    url: '/teacher/create',
    method: 'post',
    data
  })
}

export function readTeacher(data) {
  return request({
    url: '/teacher/read',
    method: 'get',
    data
  })
}

export function updateTeacher(data) {
  return request({
    url: '/teacher/update',
    method: 'post',
    data
  })
}

export function deleteTeacher(data) {
  return request({
    url: '/teacher/delete',
    method: 'post',
    data
  })
}

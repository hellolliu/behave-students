import request from '@/utils/request'

export function listStudent(query) {
  return request({
    url: '/student/list',
    method: 'get',
    params: query
  })
}

export function readStudent(data) {
  return request({
    url: '/student/read',
    method: 'get',
    params:data
  })
}
export function deleteStudent(data) {
  return request({
    url: '/student/deleted',
    method: 'post',
    data
  })
}

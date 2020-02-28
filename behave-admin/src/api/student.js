import request from '@/utils/request'

export function listStudent(query) {
  return request({
    url: '/user/listStudent',
    method: 'get',
    params: query
  })
}

export function readStudent(data) {
  return request({
    url: '/user/readStudent',
    method: 'get',
    params:data
  })
}

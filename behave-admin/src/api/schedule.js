import request from '@/utils/request'

export function querySoltAll() {
  return request({
    url: '/scheduleSolt/all',
    method: 'get'
  })
}

export function queryClassAll() {
  return request({
    url: '/class/all',
    method: 'get'
  })
}
export function queryCourseAll() {
  return request({
    url: '/course/all',
    method: 'get'
  })
}
export function updateSchedule(data) {
  return request({
    url: '/schedule/updateValue',
    method: 'post',
    data
  })
}

export function deletedSchedule(query) {
  return request({
    url: '/schedule/delete',
    method: 'post',
    params: query
  })
}
export function listScheduleValue(query) {
  return request({
    url: '/schedule/list',
    method: 'get',
    params:query
  })
}

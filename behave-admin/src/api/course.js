import request from '@/utils/request'
import axios from 'axios'
export function listCourse(query) {
  return request({
    url: '/course/list',
    method: 'get',
    params: query
  })
}

export function createCourse(data) {
  return request({
    url: '/course/create',
    method: 'post',
    data
  })
}

export function updateCourse(data) {
  return request({
    url: '/course/update',
    method: 'post',
    data
  })
}

export function deleteCourse(data) {
  return request({
    url: '/course/delete',
    method: 'post',
    data
  })
}

export function exportScore(data) {
  return request({
    url: '/score/export',
    responseType: 'blob',
    method: 'post',
    params: data
  })
}
    // 导出
  export function exportExcel(data) {
    return request({
      url: '/score/export?teacherid='+data.teacherid+"&courseid="+data.courseid,
      method: 'post',
      responseType: 'blob',
      data: data
    })
  }

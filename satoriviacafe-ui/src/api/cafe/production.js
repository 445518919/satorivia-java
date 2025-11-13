import request from '@/utils/request'

// 查询产品列表
export function listProduction(query) {
  return request({
    url: '/cafe/production/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getProduction(prodId) {
  return request({
    url: '/cafe/production/' + prodId,
    method: 'get'
  })
}

// 新增产品
export function addProduction(data) {
  return request({
    url: '/cafe/production',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateProduction(data) {
  return request({
    url: '/cafe/production',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delProduction(prodId) {
  return request({
    url: '/cafe/production/' + prodId,
    method: 'delete'
  })
}

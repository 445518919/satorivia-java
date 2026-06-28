import request from '@/utils/request'

// 查询咖啡商品冲煮方案列表
export function listBrew(query) {
    return request({
        url: '/cafe/brew/list',
        method: 'get',
        params: query
    })
}

// 查询咖啡商品冲煮方案详细
export function getBrew(brewId) {
    return request({
        url: '/cafe/brew/' + brewId,
        method: 'get'
    })
}

// 新增咖啡商品冲煮方案
export function addBrew(data) {
    return request({
        url: '/cafe/brew',
        method: 'post',
        data: data
    })
}

// 修改咖啡商品冲煮方案
export function updateBrew(data) {
    return request({
        url: '/cafe/brew',
        method: 'put',
        data: data
    })
}

// 删除咖啡商品冲煮方案
export function delBrew(brewId) {
    return request({
        url: '/cafe/brew/' + brewId,
        method: 'delete'
    })
}

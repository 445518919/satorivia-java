import request from '@/utils/request'

// 查询咖啡商品规格列表
export function listVariant(query) {
    return request({
        url: '/cafe/variant/list',
        method: 'get',
        params: query
    })
}

// 查询咖啡商品规格详细
export function getVariant(variantId) {
    return request({
        url: '/cafe/variant/' + variantId,
        method: 'get'
    })
}

// 新增咖啡商品规格
export function addVariant(data) {
    return request({
        url: '/cafe/variant',
        method: 'post',
        data: data
    })
}

// 修改咖啡商品规格
export function updateVariant(data) {
    return request({
        url: '/cafe/variant',
        method: 'put',
        data: data
    })
}

// 删除咖啡商品规格
export function delVariant(variantId) {
    return request({
        url: '/cafe/variant/' + variantId,
        method: 'delete'
    })
}

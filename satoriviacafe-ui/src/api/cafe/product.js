import request from '@/utils/request'

// 查询咖啡商品主列表
export function listProduct(query) {
    return request({
        url: '/cafe/product/list',
        method: 'get',
        params: query
    })
}

// 查询咖啡商品主详细
export function getProduct(productId) {
    return request({
        url: '/cafe/product/' + productId,
        method: 'get'
    })
}

// 新增咖啡商品主
export function addProduct(data) {
    return request({
        url: '/cafe/product',
        method: 'post',
        data: data
    })
}

// 修改咖啡商品主
export function updateProduct(data) {
    return request({
        url: '/cafe/product',
        method: 'put',
        data: data
    })
}

// 删除咖啡商品主
export function delProduct(productId) {
    return request({
        url: '/cafe/product/' + productId,
        method: 'delete'
    })
}

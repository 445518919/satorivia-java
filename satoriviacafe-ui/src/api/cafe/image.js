import request from '@/utils/request'

// 查询咖啡商品图库列表
export function listImage(query) {
    return request({
        url: '/cafe/image/list',
        method: 'get',
        params: query
    })
}

// 查询咖啡商品图库详细
export function getImage(imageId) {
    return request({
        url: '/cafe/image/' + imageId,
        method: 'get'
    })
}

// 新增咖啡商品图库
export function addImage(data) {
    return request({
        url: '/cafe/image',
        method: 'post',
        data: data
    })
}

// 修改咖啡商品图库
export function updateImage(data) {
    return request({
        url: '/cafe/image',
        method: 'put',
        data: data
    })
}

// 删除咖啡商品图库
export function delImage(imageId) {
    return request({
        url: '/cafe/image/' + imageId,
        method: 'delete'
    })
}

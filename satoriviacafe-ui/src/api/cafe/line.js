import request from '@/utils/request'

// 查询咖啡产品线列表
export function listLine(query) {
    return request({
        url: '/cafe/line/list',
        method: 'get',
        params: query
    })
}

// 查询咖啡产品线详细
export function getLine(lineId) {
    return request({
        url: '/cafe/line/' + lineId,
        method: 'get'
    })
}

// 新增咖啡产品线
export function addLine(data) {
    return request({
        url: '/cafe/line',
        method: 'post',
        data: data
    })
}

// 修改咖啡产品线
export function updateLine(data) {
    return request({
        url: '/cafe/line',
        method: 'put',
        data: data
    })
}

// 删除咖啡产品线
export function delLine(lineId) {
    return request({
        url: '/cafe/line/' + lineId,
        method: 'delete'
    })
}

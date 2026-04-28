import request from '@/utils/request'

// 查询咖啡产品访问跟踪日志列表
export function listTrack_log(query) {
    return request({
        url: '/cafe/track_log/list',
        method: 'get',
        params: query
    })
}

// 查询咖啡产品访问跟踪日志详细
export function getTrack_log(id) {
    return request({
        url: '/cafe/track_log/' + id,
        method: 'get'
    })
}

// 新增咖啡产品访问跟踪日志
export function addTrack_log(data) {
    return request({
        url: '/cafe/track_log',
        method: 'post',
        data: data
    })
}

// 修改咖啡产品访问跟踪日志
export function updateTrack_log(data) {
    return request({
        url: '/cafe/track_log',
        method: 'put',
        data: data
    })
}

// 删除咖啡产品访问跟踪日志
export function delTrack_log(id) {
    return request({
        url: '/cafe/track_log/' + id,
        method: 'delete'
    })
}

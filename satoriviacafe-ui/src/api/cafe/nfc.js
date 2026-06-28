import request from '@/utils/request'

// 查询产品NFC列表
export function listNfc(query) {
    return request({
        url: '/cafe/nfc/list',
        method: 'get',
        params: query
    })
}

// 查询产品NFC详细
export function getNfc(nfcId) {
    return request({
        url: '/cafe/nfc/' + nfcId,
        method: 'get'
    })
}

// 新增产品NFC
export function addNfc(data) {
    return request({
        url: '/cafe/nfc',
        method: 'post',
        data: data
    })
}

// 修改产品NFC
export function updateNfc(data) {
    return request({
        url: '/cafe/nfc',
        method: 'put',
        data: data
    })
}

// 删除产品NFC
export function delNfc(nfcId) {
    return request({
        url: '/cafe/nfc/' + nfcId,
        method: 'delete'
    })
}

import request from '@/utils/request'

// 查询豆子笔记列表
export function listNote(query) {
    return request({
        url: '/cafe/note/list',
        method: 'get',
        params: query
    })
}

// 查询豆子笔记详细
export function getNote(noteId) {
    return request({
        url: '/cafe/note/' + noteId,
        method: 'get'
    })
}

// 新增豆子笔记
export function addNote(data) {
    return request({
        url: '/cafe/note',
        method: 'post',
        data: data
    })
}

// 修改豆子笔记
export function updateNote(data) {
    return request({
        url: '/cafe/note',
        method: 'put',
        data: data
    })
}

// 删除豆子笔记
export function delNote(noteId) {
    return request({
        url: '/cafe/note/' + noteId,
        method: 'delete'
    })
}

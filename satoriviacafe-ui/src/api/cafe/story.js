import request from '@/utils/request'

// 查询品牌故事列表
export function listStory(query) {
  return request({
    url: '/cafe/story/list',
    method: 'get',
    params: query
  })
}

// 查询品牌故事详细
export function getStory(storyId) {
  return request({
    url: '/cafe/story/' + storyId,
    method: 'get'
  })
}

// 新增品牌故事
export function addStory(data) {
  return request({
    url: '/cafe/story',
    method: 'post',
    data: data
  })
}

// 修改品牌故事
export function updateStory(data) {
  return request({
    url: '/cafe/story',
    method: 'put',
    data: data
  })
}

// 删除品牌故事
export function delStory(storyId) {
  return request({
    url: '/cafe/story/' + storyId,
    method: 'delete'
  })
}

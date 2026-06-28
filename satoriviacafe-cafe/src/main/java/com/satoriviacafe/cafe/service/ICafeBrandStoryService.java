package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeBrandStory;

import java.util.List;

/**
 * 品牌故事主Service接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeBrandStoryService {

    /**
     * 查询品牌故事主
     *
     * @param storyId 品牌故事主主键
     * @return 品牌故事主
     */
    CafeBrandStory selectCafeBrandStoryByStoryId(Long storyId);

    /**
     * 查询品牌故事主列表
     *
     * @param cafeBrandStory 品牌故事主
     * @return 品牌故事主集合
     */
    List<CafeBrandStory> selectCafeBrandStoryList(CafeBrandStory cafeBrandStory);

    /**
     * 新增品牌故事主
     *
     * @param cafeBrandStory 品牌故事主
     * @return 结果
     */
    int insertCafeBrandStory(CafeBrandStory cafeBrandStory);

    /**
     * 批量新增品牌故事主
     *
     * @param cafeBrandStorys 品牌故事主
     * @param ignorePk 是否忽略主键
     * @return 结果
     */
    int insertBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys, boolean ignorePk);

    /**
     * 批量新增品牌故事主
     *
     * @param cafeBrandStorys 品牌故事主
     * @return 结果
     */
    default int insertBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys) {
        return insertBatchCafeBrandStory(cafeBrandStorys, false);
    }


    /**
     * 修改品牌故事主
     *
     * @param cafeBrandStory 品牌故事主
     * @return 结果
     */
    int updateCafeBrandStory(CafeBrandStory cafeBrandStory);

    /**
     * 批量修改品牌故事主
     *
     * @param cafeBrandStorys 品牌故事主
     * @return 结果
     */
    int updateBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys);

    /**
     * 批量删除品牌故事主
     *
     * @param storyIds 需要删除的品牌故事主主键集合
     * @return 结果
     */
    int deleteCafeBrandStoryByStoryIds(Long[] storyIds);

    /**
     * 删除品牌故事主信息
     *
     * @param storyId 品牌故事主主键
     * @return 结果
     */
    int deleteCafeBrandStoryByStoryId(Long storyId);
}

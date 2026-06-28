package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeBrandStory;

import java.util.List;

/**
 * 品牌故事Service接口
 *
 * @author satoriviacafe
 * @since 2025-11-13
 */
public interface ICafeBrandStoryService {

    /**
     * 查询品牌故事
     *
     * @param storyId 品牌故事主键
     * @return 品牌故事
     */
    CafeBrandStory selectCafeBrandStoryByStoryId(Long storyId);

    /**
     * 查询品牌故事列表
     *
     * @param cafeBrandStory 品牌故事
     * @return 品牌故事集合
     */
    List<CafeBrandStory> selectCafeBrandStoryList(CafeBrandStory cafeBrandStory);

    /**
     * 新增品牌故事
     *
     * @param cafeBrandStory 品牌故事
     * @return 结果
     */
    int insertCafeBrandStory(CafeBrandStory cafeBrandStory);

    /**
     * 批量新增品牌故事
     *
     * @param cafeBrandStorys 品牌故事
     * @param ignorePk        是否忽略主键
     * @return 结果
     */
    int insertBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys, boolean ignorePk);

    /**
     * 批量新增品牌故事
     *
     * @param cafeBrandStorys 品牌故事
     * @return 结果
     */
    default int insertBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys) {
        return insertBatchCafeBrandStory(cafeBrandStorys, false);
    }


    /**
     * 修改品牌故事
     *
     * @param cafeBrandStory 品牌故事
     * @return 结果
     */
    int updateCafeBrandStory(CafeBrandStory cafeBrandStory);

    /**
     * 批量修改品牌故事
     *
     * @param cafeBrandStorys 品牌故事
     * @return 结果
     */
    int updateBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys);

    /**
     * 批量删除品牌故事
     *
     * @param storyIds 需要删除的品牌故事主键集合
     * @return 结果
     */
    int deleteCafeBrandStoryByStoryIds(Long[] storyIds);

    /**
     * 删除品牌故事信息
     *
     * @param storyId 品牌故事主键
     * @return 结果
     */
    int deleteCafeBrandStoryByStoryId(Long storyId);
}

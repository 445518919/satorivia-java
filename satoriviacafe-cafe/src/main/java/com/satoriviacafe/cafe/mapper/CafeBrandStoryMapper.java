package com.satoriviacafe.cafe.mapper;

import java.util.List;
import com.satoriviacafe.cafe.domain.CafeBrandStory;

/**
 * 品牌故事Mapper接口
 *
 * @author satoriviacafe
 * @since 2025-11-13
 */
public interface CafeBrandStoryMapper {
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
     * @return 结果
     */
    int insertBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys);


    /**
     * 忽略主键批量新增品牌故事
     *
     * @param cafeBrandStorys 品牌故事
     * @return 结果
     */
    int insertBatchIgnoreCafeBrandStory(List<CafeBrandStory> cafeBrandStorys);

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
     * 删除品牌故事
     *
     * @param storyId 品牌故事主键
     * @return 结果
     */
    int deleteCafeBrandStoryByStoryId(Long storyId);

    /**
     * 批量删除品牌故事
     *
     * @param storyIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeBrandStoryByStoryIds(Long[] storyIds);
}

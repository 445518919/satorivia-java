package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeBrandStory;
import com.satoriviacafe.cafe.mapper.CafeBrandStoryMapper;
import com.satoriviacafe.cafe.service.ICafeBrandStoryService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 品牌故事Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeBrandStoryServiceImpl implements ICafeBrandStoryService {

    private final CafeBrandStoryMapper cafeBrandStoryMapper;

    /**
     * 查询品牌故事
     *
     * @param storyId 品牌故事主键
     * @return 品牌故事
     */
    @Override
    public CafeBrandStory selectCafeBrandStoryByStoryId(Long storyId) {
        return cafeBrandStoryMapper.selectCafeBrandStoryByStoryId(storyId);
    }

    /**
     * 查询品牌故事列表
     *
     * @param cafeBrandStory 品牌故事
     * @return 品牌故事
     */
    @Override
    public List<CafeBrandStory> selectCafeBrandStoryList(CafeBrandStory cafeBrandStory) {
        return cafeBrandStoryMapper.selectCafeBrandStoryList(cafeBrandStory);
    }

    /**
     * 新增品牌故事
     *
     * @param cafeBrandStory 品牌故事
     * @return 结果
     */
    @Override
    public int insertCafeBrandStory(CafeBrandStory cafeBrandStory) {
        cafeBrandStory.setCreateTime(DateUtils.getNowDate());
        return cafeBrandStoryMapper.insertCafeBrandStory(cafeBrandStory);
    }

    /**
     * 批量新增品牌故事
     *
     * @param cafeBrandStorys 品牌故事
     * @param ignorePk        是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys, boolean ignorePk) {
        for (CafeBrandStory cafeBrandStory : cafeBrandStorys) {
            cafeBrandStory.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeBrandStoryMapper.insertBatchIgnoreCafeBrandStory(cafeBrandStorys);
        }
        return cafeBrandStoryMapper.insertBatchCafeBrandStory(cafeBrandStorys);
    }

    /**
     * 修改品牌故事
     *
     * @param cafeBrandStory 品牌故事
     * @return 结果
     */
    @Override
    public int updateCafeBrandStory(CafeBrandStory cafeBrandStory) {
        cafeBrandStory.setUpdateTime(DateUtils.getNowDate());
        return cafeBrandStoryMapper.updateCafeBrandStory(cafeBrandStory);
    }

    /**
     * 批量修改品牌故事
     *
     * @param cafeBrandStorys 品牌故事
     * @return 结果
     */
    @Override
    public int updateBatchCafeBrandStory(List<CafeBrandStory> cafeBrandStorys) {
        for (CafeBrandStory cafeBrandStory : cafeBrandStorys) {
            cafeBrandStory.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeBrandStoryMapper.updateBatchCafeBrandStory(cafeBrandStorys);
    }

    /**
     * 批量删除品牌故事
     *
     * @param storyIds 需要删除的品牌故事主键
     * @return 结果
     */
    @Override
    public int deleteCafeBrandStoryByStoryIds(Long[] storyIds) {
        return cafeBrandStoryMapper.deleteCafeBrandStoryByStoryIds(storyIds);
    }

    /**
     * 删除品牌故事信息
     *
     * @param storyId 品牌故事主键
     * @return 结果
     */
    @Override
    public int deleteCafeBrandStoryByStoryId(Long storyId) {
        return cafeBrandStoryMapper.deleteCafeBrandStoryByStoryId(storyId);
    }
}

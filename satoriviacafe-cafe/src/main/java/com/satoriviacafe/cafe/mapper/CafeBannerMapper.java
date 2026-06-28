package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeBanner;

import java.util.List;

/**
 * 轮播图Mapper接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface CafeBannerMapper {
    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图主键
     * @return 轮播图
     */
    CafeBanner selectCafeBannerByBannerId(Long bannerId);

    /**
     * 查询轮播图列表
     *
     * @param cafeBanner 轮播图
     * @return 轮播图集合
     */
    List<CafeBanner> selectCafeBannerList(CafeBanner cafeBanner);

    /**
     * 新增轮播图
     *
     * @param cafeBanner 轮播图
     * @return 结果
     */
    int insertCafeBanner(CafeBanner cafeBanner);

    /**
     * 批量新增轮播图
     *
     * @param cafeBanners 轮播图
     * @return 结果
     */
    int insertBatchCafeBanner(List<CafeBanner> cafeBanners);


    /**
     * 忽略主键批量新增轮播图
     *
     * @param cafeBanners 轮播图
     * @return 结果
     */
    int insertBatchIgnoreCafeBanner(List<CafeBanner> cafeBanners);

    /**
     * 修改轮播图
     *
     * @param cafeBanner 轮播图
     * @return 结果
     */
    int updateCafeBanner(CafeBanner cafeBanner);

    /**
     * 批量修改轮播图
     *
     * @param cafeBanners 轮播图
     * @return 结果
     */
    int updateBatchCafeBanner(List<CafeBanner> cafeBanners);

    /**
     * 删除轮播图
     *
     * @param bannerId 轮播图主键
     * @return 结果
     */
    int deleteCafeBannerByBannerId(Long bannerId);

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeBannerByBannerIds(Long[] bannerIds);
}

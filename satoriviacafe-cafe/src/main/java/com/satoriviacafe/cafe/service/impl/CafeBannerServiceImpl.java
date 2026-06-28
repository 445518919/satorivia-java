package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeBanner;
import com.satoriviacafe.cafe.mapper.CafeBannerMapper;
import com.satoriviacafe.cafe.service.ICafeBannerService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeBannerServiceImpl implements ICafeBannerService {

    private final CafeBannerMapper cafeBannerMapper;

    /**
     * 查询轮播图
     *
     * @param bannerId 轮播图主键
     * @return 轮播图
     */
    @Override
    public CafeBanner selectCafeBannerByBannerId(Long bannerId) {
        return cafeBannerMapper.selectCafeBannerByBannerId(bannerId);
    }

    /**
     * 查询轮播图列表
     *
     * @param cafeBanner 轮播图
     * @return 轮播图
     */
    @Override
    public List<CafeBanner> selectCafeBannerList(CafeBanner cafeBanner) {
        return cafeBannerMapper.selectCafeBannerList(cafeBanner);
    }

    /**
     * 新增轮播图
     *
     * @param cafeBanner 轮播图
     * @return 结果
     */
    @Override
    public int insertCafeBanner(CafeBanner cafeBanner) {
        cafeBanner.setCreateTime(DateUtils.getNowDate());
        return cafeBannerMapper.insertCafeBanner(cafeBanner);
    }

    /**
     * 批量新增轮播图
     *
     * @param cafeBanners 轮播图
     * @param ignorePk    是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeBanner(List<CafeBanner> cafeBanners, boolean ignorePk) {
        for (CafeBanner cafeBanner : cafeBanners) {
            cafeBanner.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeBannerMapper.insertBatchIgnoreCafeBanner(cafeBanners);
        }
        return cafeBannerMapper.insertBatchCafeBanner(cafeBanners);
    }

    /**
     * 修改轮播图
     *
     * @param cafeBanner 轮播图
     * @return 结果
     */
    @Override
    public int updateCafeBanner(CafeBanner cafeBanner) {
        cafeBanner.setUpdateTime(DateUtils.getNowDate());
        return cafeBannerMapper.updateCafeBanner(cafeBanner);
    }

    /**
     * 批量修改轮播图
     *
     * @param cafeBanners 轮播图
     * @return 结果
     */
    @Override
    public int updateBatchCafeBanner(List<CafeBanner> cafeBanners) {
        for (CafeBanner cafeBanner : cafeBanners) {
            cafeBanner.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeBannerMapper.updateBatchCafeBanner(cafeBanners);
    }

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 需要删除的轮播图主键
     * @return 结果
     */
    @Override
    public int deleteCafeBannerByBannerIds(Long[] bannerIds) {
        return cafeBannerMapper.deleteCafeBannerByBannerIds(bannerIds);
    }

    /**
     * 删除轮播图信息
     *
     * @param bannerId 轮播图主键
     * @return 结果
     */
    @Override
    public int deleteCafeBannerByBannerId(Long bannerId) {
        return cafeBannerMapper.deleteCafeBannerByBannerId(bannerId);
    }
}

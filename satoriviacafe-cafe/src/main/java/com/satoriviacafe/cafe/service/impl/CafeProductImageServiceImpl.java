package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProductImage;
import com.satoriviacafe.cafe.mapper.CafeProductImageMapper;
import com.satoriviacafe.cafe.service.ICafeProductImageService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咖啡商品图库Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeProductImageServiceImpl implements ICafeProductImageService {

    private final CafeProductImageMapper cafeProductImageMapper;

    /**
     * 查询咖啡商品图库
     *
     * @param imageId 咖啡商品图库主键
     * @return 咖啡商品图库
     */
    @Override
    public CafeProductImage selectCafeProductImageByImageId(Long imageId) {
        return cafeProductImageMapper.selectCafeProductImageByImageId(imageId);
    }

    /**
     * 查询咖啡商品图库列表
     *
     * @param cafeProductImage 咖啡商品图库
     * @return 咖啡商品图库
     */
    @Override
    public List<CafeProductImage> selectCafeProductImageList(CafeProductImage cafeProductImage) {
        return cafeProductImageMapper.selectCafeProductImageList(cafeProductImage);
    }

    /**
     * 新增咖啡商品图库
     *
     * @param cafeProductImage 咖啡商品图库
     * @return 结果
     */
    @Override
    public int insertCafeProductImage(CafeProductImage cafeProductImage) {
        cafeProductImage.setCreateTime(DateUtils.getNowDate());
        return cafeProductImageMapper.insertCafeProductImage(cafeProductImage);
    }

    /**
     * 批量新增咖啡商品图库
     *
     * @param cafeProductImages 咖啡商品图库
     * @param ignorePk          是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProductImage(List<CafeProductImage> cafeProductImages, boolean ignorePk) {
        for (CafeProductImage cafeProductImage : cafeProductImages) {
            cafeProductImage.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductImageMapper.insertBatchIgnoreCafeProductImage(cafeProductImages);
        }
        return cafeProductImageMapper.insertBatchCafeProductImage(cafeProductImages);
    }

    /**
     * 修改咖啡商品图库
     *
     * @param cafeProductImage 咖啡商品图库
     * @return 结果
     */
    @Override
    public int updateCafeProductImage(CafeProductImage cafeProductImage) {
        cafeProductImage.setUpdateTime(DateUtils.getNowDate());
        return cafeProductImageMapper.updateCafeProductImage(cafeProductImage);
    }

    /**
     * 批量修改咖啡商品图库
     *
     * @param cafeProductImages 咖啡商品图库
     * @return 结果
     */
    @Override
    public int updateBatchCafeProductImage(List<CafeProductImage> cafeProductImages) {
        for (CafeProductImage cafeProductImage : cafeProductImages) {
            cafeProductImage.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductImageMapper.updateBatchCafeProductImage(cafeProductImages);
    }

    /**
     * 批量删除咖啡商品图库
     *
     * @param imageIds 需要删除的咖啡商品图库主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductImageByImageIds(Long[] imageIds) {
        return cafeProductImageMapper.deleteCafeProductImageByImageIds(imageIds);
    }

    /**
     * 删除咖啡商品图库信息
     *
     * @param imageId 咖啡商品图库主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductImageByImageId(Long imageId) {
        return cafeProductImageMapper.deleteCafeProductImageByImageId(imageId);
    }
}

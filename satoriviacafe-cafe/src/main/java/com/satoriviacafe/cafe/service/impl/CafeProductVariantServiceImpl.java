package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProductVariant;
import com.satoriviacafe.cafe.mapper.CafeProductVariantMapper;
import com.satoriviacafe.cafe.service.ICafeProductVariantService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咖啡商品规格Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeProductVariantServiceImpl implements ICafeProductVariantService {

    private final CafeProductVariantMapper cafeProductVariantMapper;

    /**
     * 查询咖啡商品规格
     *
     * @param variantId 咖啡商品规格主键
     * @return 咖啡商品规格
     */
    @Override
    public CafeProductVariant selectCafeProductVariantByVariantId(Long variantId) {
        return cafeProductVariantMapper.selectCafeProductVariantByVariantId(variantId);
    }

    /**
     * 查询咖啡商品规格列表
     *
     * @param cafeProductVariant 咖啡商品规格
     * @return 咖啡商品规格
     */
    @Override
    public List<CafeProductVariant> selectCafeProductVariantList(CafeProductVariant cafeProductVariant) {
        return cafeProductVariantMapper.selectCafeProductVariantList(cafeProductVariant);
    }

    /**
     * 新增咖啡商品规格
     *
     * @param cafeProductVariant 咖啡商品规格
     * @return 结果
     */
    @Override
    public int insertCafeProductVariant(CafeProductVariant cafeProductVariant) {
        cafeProductVariant.setCreateTime(DateUtils.getNowDate());
        return cafeProductVariantMapper.insertCafeProductVariant(cafeProductVariant);
    }

    /**
     * 批量新增咖啡商品规格
     *
     * @param cafeProductVariants 咖啡商品规格
     * @param ignorePk            是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProductVariant(List<CafeProductVariant> cafeProductVariants, boolean ignorePk) {
        for (CafeProductVariant cafeProductVariant : cafeProductVariants) {
            cafeProductVariant.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductVariantMapper.insertBatchIgnoreCafeProductVariant(cafeProductVariants);
        }
        return cafeProductVariantMapper.insertBatchCafeProductVariant(cafeProductVariants);
    }

    /**
     * 修改咖啡商品规格
     *
     * @param cafeProductVariant 咖啡商品规格
     * @return 结果
     */
    @Override
    public int updateCafeProductVariant(CafeProductVariant cafeProductVariant) {
        cafeProductVariant.setUpdateTime(DateUtils.getNowDate());
        return cafeProductVariantMapper.updateCafeProductVariant(cafeProductVariant);
    }

    /**
     * 批量修改咖啡商品规格
     *
     * @param cafeProductVariants 咖啡商品规格
     * @return 结果
     */
    @Override
    public int updateBatchCafeProductVariant(List<CafeProductVariant> cafeProductVariants) {
        for (CafeProductVariant cafeProductVariant : cafeProductVariants) {
            cafeProductVariant.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductVariantMapper.updateBatchCafeProductVariant(cafeProductVariants);
    }

    /**
     * 批量删除咖啡商品规格
     *
     * @param variantIds 需要删除的咖啡商品规格主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductVariantByVariantIds(Long[] variantIds) {
        return cafeProductVariantMapper.deleteCafeProductVariantByVariantIds(variantIds);
    }

    /**
     * 删除咖啡商品规格信息
     *
     * @param variantId 咖啡商品规格主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductVariantByVariantId(Long variantId) {
        return cafeProductVariantMapper.deleteCafeProductVariantByVariantId(variantId);
    }
}

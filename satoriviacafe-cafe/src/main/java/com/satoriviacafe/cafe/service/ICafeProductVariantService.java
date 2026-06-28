package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeProductVariant;

import java.util.List;

/**
 * 咖啡商品规格Service接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeProductVariantService {

    /**
     * 查询咖啡商品规格
     *
     * @param variantId 咖啡商品规格主键
     * @return 咖啡商品规格
     */
    CafeProductVariant selectCafeProductVariantByVariantId(Long variantId);

    /**
     * 查询咖啡商品规格列表
     *
     * @param cafeProductVariant 咖啡商品规格
     * @return 咖啡商品规格集合
     */
    List<CafeProductVariant> selectCafeProductVariantList(CafeProductVariant cafeProductVariant);

    /**
     * 新增咖啡商品规格
     *
     * @param cafeProductVariant 咖啡商品规格
     * @return 结果
     */
    int insertCafeProductVariant(CafeProductVariant cafeProductVariant);

    /**
     * 批量新增咖啡商品规格
     *
     * @param cafeProductVariants 咖啡商品规格
     * @param ignorePk            是否忽略主键
     * @return 结果
     */
    int insertBatchCafeProductVariant(List<CafeProductVariant> cafeProductVariants, boolean ignorePk);

    /**
     * 批量新增咖啡商品规格
     *
     * @param cafeProductVariants 咖啡商品规格
     * @return 结果
     */
    default int insertBatchCafeProductVariant(List<CafeProductVariant> cafeProductVariants) {
        return insertBatchCafeProductVariant(cafeProductVariants, false);
    }


    /**
     * 修改咖啡商品规格
     *
     * @param cafeProductVariant 咖啡商品规格
     * @return 结果
     */
    int updateCafeProductVariant(CafeProductVariant cafeProductVariant);

    /**
     * 批量修改咖啡商品规格
     *
     * @param cafeProductVariants 咖啡商品规格
     * @return 结果
     */
    int updateBatchCafeProductVariant(List<CafeProductVariant> cafeProductVariants);

    /**
     * 批量删除咖啡商品规格
     *
     * @param variantIds 需要删除的咖啡商品规格主键集合
     * @return 结果
     */
    int deleteCafeProductVariantByVariantIds(Long[] variantIds);

    /**
     * 删除咖啡商品规格信息
     *
     * @param variantId 咖啡商品规格主键
     * @return 结果
     */
    int deleteCafeProductVariantByVariantId(Long variantId);
}

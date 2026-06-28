package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeProductVariant;

import java.util.List;

/**
 * 咖啡商品规格Mapper接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface CafeProductVariantMapper {
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
     * @return 结果
     */
    int insertBatchCafeProductVariant(List<CafeProductVariant> cafeProductVariants);


    /**
     * 忽略主键批量新增咖啡商品规格
     *
     * @param cafeProductVariants 咖啡商品规格
     * @return 结果
     */
    int insertBatchIgnoreCafeProductVariant(List<CafeProductVariant> cafeProductVariants);

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
     * 删除咖啡商品规格
     *
     * @param variantId 咖啡商品规格主键
     * @return 结果
     */
    int deleteCafeProductVariantByVariantId(Long variantId);

    /**
     * 批量删除咖啡商品规格
     *
     * @param variantIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeProductVariantByVariantIds(Long[] variantIds);
}

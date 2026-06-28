package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeProductImage;

import java.util.List;

/**
 * 咖啡商品图库Mapper接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface CafeProductImageMapper {
    /**
     * 查询咖啡商品图库
     *
     * @param imageId 咖啡商品图库主键
     * @return 咖啡商品图库
     */
    CafeProductImage selectCafeProductImageByImageId(Long imageId);

    /**
     * 查询咖啡商品图库列表
     *
     * @param cafeProductImage 咖啡商品图库
     * @return 咖啡商品图库集合
     */
    List<CafeProductImage> selectCafeProductImageList(CafeProductImage cafeProductImage);

    /**
     * 新增咖啡商品图库
     *
     * @param cafeProductImage 咖啡商品图库
     * @return 结果
     */
    int insertCafeProductImage(CafeProductImage cafeProductImage);

    /**
     * 批量新增咖啡商品图库
     *
     * @param cafeProductImages 咖啡商品图库
     * @return 结果
     */
    int insertBatchCafeProductImage(List<CafeProductImage> cafeProductImages);


    /**
     * 忽略主键批量新增咖啡商品图库
     *
     * @param cafeProductImages 咖啡商品图库
     * @return 结果
     */
    int insertBatchIgnoreCafeProductImage(List<CafeProductImage> cafeProductImages);

    /**
     * 修改咖啡商品图库
     *
     * @param cafeProductImage 咖啡商品图库
     * @return 结果
     */
    int updateCafeProductImage(CafeProductImage cafeProductImage);

    /**
     * 批量修改咖啡商品图库
     *
     * @param cafeProductImages 咖啡商品图库
     * @return 结果
     */
    int updateBatchCafeProductImage(List<CafeProductImage> cafeProductImages);

    /**
     * 删除咖啡商品图库
     *
     * @param imageId 咖啡商品图库主键
     * @return 结果
     */
    int deleteCafeProductImageByImageId(Long imageId);

    /**
     * 批量删除咖啡商品图库
     *
     * @param imageIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeProductImageByImageIds(Long[] imageIds);
}

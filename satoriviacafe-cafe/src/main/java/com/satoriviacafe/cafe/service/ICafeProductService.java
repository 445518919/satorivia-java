package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeProduct;

import java.util.List;

/**
 * 咖啡商品主Service接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeProductService {

    /**
     * 查询咖啡商品主
     *
     * @param productId 咖啡商品主主键
     * @return 咖啡商品主
     */
    CafeProduct selectCafeProductByProductId(Long productId);

    /**
     * 查询咖啡商品主列表
     *
     * @param cafeProduct 咖啡商品主
     * @return 咖啡商品主集合
     */
    List<CafeProduct> selectCafeProductList(CafeProduct cafeProduct);

    /**
     * 新增咖啡商品主
     *
     * @param cafeProduct 咖啡商品主
     * @return 结果
     */
    int insertCafeProduct(CafeProduct cafeProduct);

    /**
     * 批量新增咖啡商品主
     *
     * @param cafeProducts 咖啡商品主
     * @param ignorePk     是否忽略主键
     * @return 结果
     */
    int insertBatchCafeProduct(List<CafeProduct> cafeProducts, boolean ignorePk);

    /**
     * 批量新增咖啡商品主
     *
     * @param cafeProducts 咖啡商品主
     * @return 结果
     */
    default int insertBatchCafeProduct(List<CafeProduct> cafeProducts) {
        return insertBatchCafeProduct(cafeProducts, false);
    }


    /**
     * 修改咖啡商品主
     *
     * @param cafeProduct 咖啡商品主
     * @return 结果
     */
    int updateCafeProduct(CafeProduct cafeProduct);

    /**
     * 批量修改咖啡商品主
     *
     * @param cafeProducts 咖啡商品主
     * @return 结果
     */
    int updateBatchCafeProduct(List<CafeProduct> cafeProducts);

    /**
     * 批量删除咖啡商品主
     *
     * @param productIds 需要删除的咖啡商品主主键集合
     * @return 结果
     */
    int deleteCafeProductByProductIds(Long[] productIds);

    /**
     * 删除咖啡商品主信息
     *
     * @param productId 咖啡商品主主键
     * @return 结果
     */
    int deleteCafeProductByProductId(Long productId);
}

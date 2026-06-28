package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProduct;
import com.satoriviacafe.cafe.mapper.CafeProductMapper;
import com.satoriviacafe.cafe.service.ICafeProductService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咖啡商品主Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeProductServiceImpl implements ICafeProductService {

    private final CafeProductMapper cafeProductMapper;

    /**
     * 查询咖啡商品主
     *
     * @param productId 咖啡商品主主键
     * @return 咖啡商品主
     */
    @Override
    public CafeProduct selectCafeProductByProductId(Long productId) {
        return cafeProductMapper.selectCafeProductByProductId(productId);
    }

    /**
     * 查询咖啡商品主列表
     *
     * @param cafeProduct 咖啡商品主
     * @return 咖啡商品主
     */
    @Override
    public List<CafeProduct> selectCafeProductList(CafeProduct cafeProduct) {
        return cafeProductMapper.selectCafeProductList(cafeProduct);
    }

    /**
     * 新增咖啡商品主
     *
     * @param cafeProduct 咖啡商品主
     * @return 结果
     */
    @Override
    public int insertCafeProduct(CafeProduct cafeProduct) {
        cafeProduct.setCreateTime(DateUtils.getNowDate());
        return cafeProductMapper.insertCafeProduct(cafeProduct);
    }

    /**
     * 批量新增咖啡商品主
     *
     * @param cafeProducts 咖啡商品主
     * @param ignorePk     是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProduct(List<CafeProduct> cafeProducts, boolean ignorePk) {
        for (CafeProduct cafeProduct : cafeProducts) {
            cafeProduct.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductMapper.insertBatchIgnoreCafeProduct(cafeProducts);
        }
        return cafeProductMapper.insertBatchCafeProduct(cafeProducts);
    }

    /**
     * 修改咖啡商品主
     *
     * @param cafeProduct 咖啡商品主
     * @return 结果
     */
    @Override
    public int updateCafeProduct(CafeProduct cafeProduct) {
        cafeProduct.setUpdateTime(DateUtils.getNowDate());
        return cafeProductMapper.updateCafeProduct(cafeProduct);
    }

    /**
     * 批量修改咖啡商品主
     *
     * @param cafeProducts 咖啡商品主
     * @return 结果
     */
    @Override
    public int updateBatchCafeProduct(List<CafeProduct> cafeProducts) {
        for (CafeProduct cafeProduct : cafeProducts) {
            cafeProduct.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductMapper.updateBatchCafeProduct(cafeProducts);
    }

    /**
     * 批量删除咖啡商品主
     *
     * @param productIds 需要删除的咖啡商品主主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductByProductIds(Long[] productIds) {
        return cafeProductMapper.deleteCafeProductByProductIds(productIds);
    }

    /**
     * 删除咖啡商品主信息
     *
     * @param productId 咖啡商品主主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductByProductId(Long productId) {
        return cafeProductMapper.deleteCafeProductByProductId(productId);
    }
}

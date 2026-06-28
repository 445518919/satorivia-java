package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeProduction;

import java.util.List;

/**
 * 产品Service接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeProductionService {

    /**
     * 查询产品
     *
     * @param prodId 产品主键
     * @return 产品
     */
    CafeProduction selectCafeProductionByProdId(Long prodId);

    /**
     * 查询产品列表
     *
     * @param cafeProduction 产品
     * @return 产品集合
     */
    List<CafeProduction> selectCafeProductionList(CafeProduction cafeProduction);

    /**
     * 新增产品
     *
     * @param cafeProduction 产品
     * @return 结果
     */
    int insertCafeProduction(CafeProduction cafeProduction);

    /**
     * 批量新增产品
     *
     * @param cafeProductions 产品
     * @param ignorePk        是否忽略主键
     * @return 结果
     */
    int insertBatchCafeProduction(List<CafeProduction> cafeProductions, boolean ignorePk);

    /**
     * 批量新增产品
     *
     * @param cafeProductions 产品
     * @return 结果
     */
    default int insertBatchCafeProduction(List<CafeProduction> cafeProductions) {
        return insertBatchCafeProduction(cafeProductions, false);
    }


    /**
     * 修改产品
     *
     * @param cafeProduction 产品
     * @return 结果
     */
    int updateCafeProduction(CafeProduction cafeProduction);

    /**
     * 批量修改产品
     *
     * @param cafeProductions 产品
     * @return 结果
     */
    int updateBatchCafeProduction(List<CafeProduction> cafeProductions);

    /**
     * 批量删除产品
     *
     * @param prodIds 需要删除的产品主键集合
     * @return 结果
     */
    int deleteCafeProductionByProdIds(Long[] prodIds);

    /**
     * 删除产品信息
     *
     * @param prodId 产品主键
     * @return 结果
     */
    int deleteCafeProductionByProdId(Long prodId);
}

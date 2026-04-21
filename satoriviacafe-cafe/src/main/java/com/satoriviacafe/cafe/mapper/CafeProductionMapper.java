package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeProduction;

import java.util.List;

/**
 * 产品Mapper接口
 *
 * @author satoriviacafe
 * @since 2026-04-21
 */
public interface CafeProductionMapper {
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
     * @return 结果
     */
    int insertBatchCafeProduction(List<CafeProduction> cafeProductions);


    /**
     * 忽略主键批量新增产品
     *
     * @param cafeProductions 产品
     * @return 结果
     */
    int insertBatchIgnoreCafeProduction(List<CafeProduction> cafeProductions);

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
     * 删除产品
     *
     * @param prodId 产品主键
     * @return 结果
     */
    int deleteCafeProductionByProdId(Long prodId);

    /**
     * 批量删除产品
     *
     * @param prodIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeProductionByProdIds(Long[] prodIds);
}

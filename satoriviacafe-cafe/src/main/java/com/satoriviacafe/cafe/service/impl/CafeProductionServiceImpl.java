package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProduction;
import com.satoriviacafe.cafe.mapper.CafeProductionMapper;
import com.satoriviacafe.cafe.service.ICafeProductionService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-04-21
 */
@Service
@RequiredArgsConstructor
public class CafeProductionServiceImpl implements ICafeProductionService {

    private final CafeProductionMapper cafeProductionMapper;

    /**
     * 查询产品
     *
     * @param prodId 产品主键
     * @return 产品
     */
    @Override
    public CafeProduction selectCafeProductionByProdId(Long prodId) {
        return cafeProductionMapper.selectCafeProductionByProdId(prodId);
    }

    /**
     * 查询产品列表
     *
     * @param cafeProduction 产品
     * @return 产品
     */
    @Override
    public List<CafeProduction> selectCafeProductionList(CafeProduction cafeProduction) {
        return cafeProductionMapper.selectCafeProductionList(cafeProduction);
    }

    /**
     * 新增产品
     *
     * @param cafeProduction 产品
     * @return 结果
     */
    @Override
    public int insertCafeProduction(CafeProduction cafeProduction) {
        cafeProduction.setCreateTime(DateUtils.getNowDate());
        return cafeProductionMapper.insertCafeProduction(cafeProduction);
    }

    /**
     * 批量新增产品
     *
     * @param cafeProductions 产品
     * @param ignorePk        是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProduction(List<CafeProduction> cafeProductions, boolean ignorePk) {
        for (CafeProduction cafeProduction : cafeProductions) {
            cafeProduction.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductionMapper.insertBatchIgnoreCafeProduction(cafeProductions);
        }
        return cafeProductionMapper.insertBatchCafeProduction(cafeProductions);
    }

    /**
     * 修改产品
     *
     * @param cafeProduction 产品
     * @return 结果
     */
    @Override
    public int updateCafeProduction(CafeProduction cafeProduction) {
        cafeProduction.setUpdateTime(DateUtils.getNowDate());
        return cafeProductionMapper.updateCafeProduction(cafeProduction);
    }

    /**
     * 批量修改产品
     *
     * @param cafeProductions 产品
     * @return 结果
     */
    @Override
    public int updateBatchCafeProduction(List<CafeProduction> cafeProductions) {
        for (CafeProduction cafeProduction : cafeProductions) {
            cafeProduction.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductionMapper.updateBatchCafeProduction(cafeProductions);
    }

    /**
     * 批量删除产品
     *
     * @param prodIds 需要删除的产品主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductionByProdIds(Long[] prodIds) {
        return cafeProductionMapper.deleteCafeProductionByProdIds(prodIds);
    }

    /**
     * 删除产品信息
     *
     * @param prodId 产品主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductionByProdId(Long prodId) {
        return cafeProductionMapper.deleteCafeProductionByProdId(prodId);
    }
}

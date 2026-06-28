package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProductBrew;
import com.satoriviacafe.cafe.mapper.CafeProductBrewMapper;
import com.satoriviacafe.cafe.service.ICafeProductBrewService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咖啡商品冲煮方案Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeProductBrewServiceImpl implements ICafeProductBrewService {

    private final CafeProductBrewMapper cafeProductBrewMapper;

    /**
     * 查询咖啡商品冲煮方案
     *
     * @param brewId 咖啡商品冲煮方案主键
     * @return 咖啡商品冲煮方案
     */
    @Override
    public CafeProductBrew selectCafeProductBrewByBrewId(Long brewId) {
        return cafeProductBrewMapper.selectCafeProductBrewByBrewId(brewId);
    }

    /**
     * 查询咖啡商品冲煮方案列表
     *
     * @param cafeProductBrew 咖啡商品冲煮方案
     * @return 咖啡商品冲煮方案
     */
    @Override
    public List<CafeProductBrew> selectCafeProductBrewList(CafeProductBrew cafeProductBrew) {
        return cafeProductBrewMapper.selectCafeProductBrewList(cafeProductBrew);
    }

    /**
     * 新增咖啡商品冲煮方案
     *
     * @param cafeProductBrew 咖啡商品冲煮方案
     * @return 结果
     */
    @Override
    public int insertCafeProductBrew(CafeProductBrew cafeProductBrew) {
        cafeProductBrew.setCreateTime(DateUtils.getNowDate());
        return cafeProductBrewMapper.insertCafeProductBrew(cafeProductBrew);
    }

    /**
     * 批量新增咖啡商品冲煮方案
     *
     * @param cafeProductBrews 咖啡商品冲煮方案
     * @param ignorePk         是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProductBrew(List<CafeProductBrew> cafeProductBrews, boolean ignorePk) {
        for (CafeProductBrew cafeProductBrew : cafeProductBrews) {
            cafeProductBrew.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductBrewMapper.insertBatchIgnoreCafeProductBrew(cafeProductBrews);
        }
        return cafeProductBrewMapper.insertBatchCafeProductBrew(cafeProductBrews);
    }

    /**
     * 修改咖啡商品冲煮方案
     *
     * @param cafeProductBrew 咖啡商品冲煮方案
     * @return 结果
     */
    @Override
    public int updateCafeProductBrew(CafeProductBrew cafeProductBrew) {
        cafeProductBrew.setUpdateTime(DateUtils.getNowDate());
        return cafeProductBrewMapper.updateCafeProductBrew(cafeProductBrew);
    }

    /**
     * 批量修改咖啡商品冲煮方案
     *
     * @param cafeProductBrews 咖啡商品冲煮方案
     * @return 结果
     */
    @Override
    public int updateBatchCafeProductBrew(List<CafeProductBrew> cafeProductBrews) {
        for (CafeProductBrew cafeProductBrew : cafeProductBrews) {
            cafeProductBrew.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductBrewMapper.updateBatchCafeProductBrew(cafeProductBrews);
    }

    /**
     * 批量删除咖啡商品冲煮方案
     *
     * @param brewIds 需要删除的咖啡商品冲煮方案主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductBrewByBrewIds(Long[] brewIds) {
        return cafeProductBrewMapper.deleteCafeProductBrewByBrewIds(brewIds);
    }

    /**
     * 删除咖啡商品冲煮方案信息
     *
     * @param brewId 咖啡商品冲煮方案主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductBrewByBrewId(Long brewId) {
        return cafeProductBrewMapper.deleteCafeProductBrewByBrewId(brewId);
    }
}

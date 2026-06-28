package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeProductBrew;

import java.util.List;

/**
 * 咖啡商品冲煮方案Mapper接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface CafeProductBrewMapper {
    /**
     * 查询咖啡商品冲煮方案
     *
     * @param brewId 咖啡商品冲煮方案主键
     * @return 咖啡商品冲煮方案
     */
    CafeProductBrew selectCafeProductBrewByBrewId(Long brewId);

    /**
     * 查询咖啡商品冲煮方案列表
     *
     * @param cafeProductBrew 咖啡商品冲煮方案
     * @return 咖啡商品冲煮方案集合
     */
    List<CafeProductBrew> selectCafeProductBrewList(CafeProductBrew cafeProductBrew);

    /**
     * 新增咖啡商品冲煮方案
     *
     * @param cafeProductBrew 咖啡商品冲煮方案
     * @return 结果
     */
    int insertCafeProductBrew(CafeProductBrew cafeProductBrew);

    /**
     * 批量新增咖啡商品冲煮方案
     *
     * @param cafeProductBrews 咖啡商品冲煮方案
     * @return 结果
     */
    int insertBatchCafeProductBrew(List<CafeProductBrew> cafeProductBrews);


    /**
     * 忽略主键批量新增咖啡商品冲煮方案
     *
     * @param cafeProductBrews 咖啡商品冲煮方案
     * @return 结果
     */
    int insertBatchIgnoreCafeProductBrew(List<CafeProductBrew> cafeProductBrews);

    /**
     * 修改咖啡商品冲煮方案
     *
     * @param cafeProductBrew 咖啡商品冲煮方案
     * @return 结果
     */
    int updateCafeProductBrew(CafeProductBrew cafeProductBrew);

    /**
     * 批量修改咖啡商品冲煮方案
     *
     * @param cafeProductBrews 咖啡商品冲煮方案
     * @return 结果
     */
    int updateBatchCafeProductBrew(List<CafeProductBrew> cafeProductBrews);

    /**
     * 删除咖啡商品冲煮方案
     *
     * @param brewId 咖啡商品冲煮方案主键
     * @return 结果
     */
    int deleteCafeProductBrewByBrewId(Long brewId);

    /**
     * 批量删除咖啡商品冲煮方案
     *
     * @param brewIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeProductBrewByBrewIds(Long[] brewIds);
}

package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeProductLine;

import java.util.List;

/**
 * 咖啡产品线Service接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeProductLineService {

    /**
     * 查询咖啡产品线
     *
     * @param lineId 咖啡产品线主键
     * @return 咖啡产品线
     */
    CafeProductLine selectCafeProductLineByLineId(Long lineId);

    /**
     * 查询咖啡产品线列表
     *
     * @param cafeProductLine 咖啡产品线
     * @return 咖啡产品线集合
     */
    List<CafeProductLine> selectCafeProductLineList(CafeProductLine cafeProductLine);

    /**
     * 新增咖啡产品线
     *
     * @param cafeProductLine 咖啡产品线
     * @return 结果
     */
    int insertCafeProductLine(CafeProductLine cafeProductLine);

    /**
     * 批量新增咖啡产品线
     *
     * @param cafeProductLines 咖啡产品线
     * @param ignorePk         是否忽略主键
     * @return 结果
     */
    int insertBatchCafeProductLine(List<CafeProductLine> cafeProductLines, boolean ignorePk);

    /**
     * 批量新增咖啡产品线
     *
     * @param cafeProductLines 咖啡产品线
     * @return 结果
     */
    default int insertBatchCafeProductLine(List<CafeProductLine> cafeProductLines) {
        return insertBatchCafeProductLine(cafeProductLines, false);
    }


    /**
     * 修改咖啡产品线
     *
     * @param cafeProductLine 咖啡产品线
     * @return 结果
     */
    int updateCafeProductLine(CafeProductLine cafeProductLine);

    /**
     * 批量修改咖啡产品线
     *
     * @param cafeProductLines 咖啡产品线
     * @return 结果
     */
    int updateBatchCafeProductLine(List<CafeProductLine> cafeProductLines);

    /**
     * 批量删除咖啡产品线
     *
     * @param lineIds 需要删除的咖啡产品线主键集合
     * @return 结果
     */
    int deleteCafeProductLineByLineIds(Long[] lineIds);

    /**
     * 删除咖啡产品线信息
     *
     * @param lineId 咖啡产品线主键
     * @return 结果
     */
    int deleteCafeProductLineByLineId(Long lineId);
}

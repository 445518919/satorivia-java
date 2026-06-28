package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProductLine;
import com.satoriviacafe.cafe.mapper.CafeProductLineMapper;
import com.satoriviacafe.cafe.service.ICafeProductLineService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咖啡产品线Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeProductLineServiceImpl implements ICafeProductLineService {

    private final CafeProductLineMapper cafeProductLineMapper;

    /**
     * 查询咖啡产品线
     *
     * @param lineId 咖啡产品线主键
     * @return 咖啡产品线
     */
    @Override
    public CafeProductLine selectCafeProductLineByLineId(Long lineId) {
        return cafeProductLineMapper.selectCafeProductLineByLineId(lineId);
    }

    /**
     * 查询咖啡产品线列表
     *
     * @param cafeProductLine 咖啡产品线
     * @return 咖啡产品线
     */
    @Override
    public List<CafeProductLine> selectCafeProductLineList(CafeProductLine cafeProductLine) {
        return cafeProductLineMapper.selectCafeProductLineList(cafeProductLine);
    }

    /**
     * 新增咖啡产品线
     *
     * @param cafeProductLine 咖啡产品线
     * @return 结果
     */
    @Override
    public int insertCafeProductLine(CafeProductLine cafeProductLine) {
        cafeProductLine.setCreateTime(DateUtils.getNowDate());
        return cafeProductLineMapper.insertCafeProductLine(cafeProductLine);
    }

    /**
     * 批量新增咖啡产品线
     *
     * @param cafeProductLines 咖啡产品线
     * @param ignorePk         是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProductLine(List<CafeProductLine> cafeProductLines, boolean ignorePk) {
        for (CafeProductLine cafeProductLine : cafeProductLines) {
            cafeProductLine.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductLineMapper.insertBatchIgnoreCafeProductLine(cafeProductLines);
        }
        return cafeProductLineMapper.insertBatchCafeProductLine(cafeProductLines);
    }

    /**
     * 修改咖啡产品线
     *
     * @param cafeProductLine 咖啡产品线
     * @return 结果
     */
    @Override
    public int updateCafeProductLine(CafeProductLine cafeProductLine) {
        cafeProductLine.setUpdateTime(DateUtils.getNowDate());
        return cafeProductLineMapper.updateCafeProductLine(cafeProductLine);
    }

    /**
     * 批量修改咖啡产品线
     *
     * @param cafeProductLines 咖啡产品线
     * @return 结果
     */
    @Override
    public int updateBatchCafeProductLine(List<CafeProductLine> cafeProductLines) {
        for (CafeProductLine cafeProductLine : cafeProductLines) {
            cafeProductLine.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductLineMapper.updateBatchCafeProductLine(cafeProductLines);
    }

    /**
     * 批量删除咖啡产品线
     *
     * @param lineIds 需要删除的咖啡产品线主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductLineByLineIds(Long[] lineIds) {
        return cafeProductLineMapper.deleteCafeProductLineByLineIds(lineIds);
    }

    /**
     * 删除咖啡产品线信息
     *
     * @param lineId 咖啡产品线主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductLineByLineId(Long lineId) {
        return cafeProductLineMapper.deleteCafeProductLineByLineId(lineId);
    }
}

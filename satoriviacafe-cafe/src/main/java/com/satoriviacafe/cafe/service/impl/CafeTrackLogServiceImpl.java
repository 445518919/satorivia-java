package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeTrackLog;
import com.satoriviacafe.cafe.mapper.CafeTrackLogMapper;
import com.satoriviacafe.cafe.service.ICafeTrackLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 咖啡产品访问跟踪日志Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-04-29
 */
@Service
@RequiredArgsConstructor
public class CafeTrackLogServiceImpl implements ICafeTrackLogService {

    private final CafeTrackLogMapper cafeTrackLogMapper;

    /**
     * 查询咖啡产品访问跟踪日志
     *
     * @param id 咖啡产品访问跟踪日志主键
     * @return 咖啡产品访问跟踪日志
     */
    @Override
    public CafeTrackLog selectCafeTrackLogById(Long id) {
        return cafeTrackLogMapper.selectCafeTrackLogById(id);
    }

    /**
     * 查询咖啡产品访问跟踪日志列表
     *
     * @param cafeTrackLog 咖啡产品访问跟踪日志
     * @return 咖啡产品访问跟踪日志
     */
    @Override
    public List<CafeTrackLog> selectCafeTrackLogList(CafeTrackLog cafeTrackLog) {
        return cafeTrackLogMapper.selectCafeTrackLogList(cafeTrackLog);
    }

    /**
     * 新增咖啡产品访问跟踪日志
     *
     * @param cafeTrackLog 咖啡产品访问跟踪日志
     * @return 结果
     */
    @Override
    public int insertCafeTrackLog(CafeTrackLog cafeTrackLog) {
        return cafeTrackLogMapper.insertCafeTrackLog(cafeTrackLog);
    }

    /**
     * 批量新增咖啡产品访问跟踪日志
     *
     * @param cafeTrackLogs 咖啡产品访问跟踪日志
     * @param ignorePk      是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeTrackLog(List<CafeTrackLog> cafeTrackLogs, boolean ignorePk) {
        if (ignorePk) {
            return cafeTrackLogMapper.insertBatchIgnoreCafeTrackLog(cafeTrackLogs);
        }
        return cafeTrackLogMapper.insertBatchCafeTrackLog(cafeTrackLogs);
    }

    /**
     * 修改咖啡产品访问跟踪日志
     *
     * @param cafeTrackLog 咖啡产品访问跟踪日志
     * @return 结果
     */
    @Override
    public int updateCafeTrackLog(CafeTrackLog cafeTrackLog) {
        return cafeTrackLogMapper.updateCafeTrackLog(cafeTrackLog);
    }

    /**
     * 批量修改咖啡产品访问跟踪日志
     *
     * @param cafeTrackLogs 咖啡产品访问跟踪日志
     * @return 结果
     */
    @Override
    public int updateBatchCafeTrackLog(List<CafeTrackLog> cafeTrackLogs) {
        return cafeTrackLogMapper.updateBatchCafeTrackLog(cafeTrackLogs);
    }

    /**
     * 批量删除咖啡产品访问跟踪日志
     *
     * @param ids 需要删除的咖啡产品访问跟踪日志主键
     * @return 结果
     */
    @Override
    public int deleteCafeTrackLogByIds(Long[] ids) {
        return cafeTrackLogMapper.deleteCafeTrackLogByIds(ids);
    }

    /**
     * 删除咖啡产品访问跟踪日志信息
     *
     * @param id 咖啡产品访问跟踪日志主键
     * @return 结果
     */
    @Override
    public int deleteCafeTrackLogById(Long id) {
        return cafeTrackLogMapper.deleteCafeTrackLogById(id);
    }
}

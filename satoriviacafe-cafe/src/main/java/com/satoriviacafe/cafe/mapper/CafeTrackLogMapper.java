package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeTrackLog;

import java.util.List;

/**
 * 咖啡产品访问跟踪日志Mapper接口
 *
 * @author satoriviacafe
 * @since 2026-04-29
 */
public interface CafeTrackLogMapper {
    /**
     * 查询咖啡产品访问跟踪日志
     *
     * @param id 咖啡产品访问跟踪日志主键
     * @return 咖啡产品访问跟踪日志
     */
    CafeTrackLog selectCafeTrackLogById(Long id);

    /**
     * 查询咖啡产品访问跟踪日志列表
     *
     * @param cafeTrackLog 咖啡产品访问跟踪日志
     * @return 咖啡产品访问跟踪日志集合
     */
    List<CafeTrackLog> selectCafeTrackLogList(CafeTrackLog cafeTrackLog);

    /**
     * 新增咖啡产品访问跟踪日志
     *
     * @param cafeTrackLog 咖啡产品访问跟踪日志
     * @return 结果
     */
    int insertCafeTrackLog(CafeTrackLog cafeTrackLog);

    /**
     * 批量新增咖啡产品访问跟踪日志
     *
     * @param cafeTrackLogs 咖啡产品访问跟踪日志
     * @return 结果
     */
    int insertBatchCafeTrackLog(List<CafeTrackLog> cafeTrackLogs);


    /**
     * 忽略主键批量新增咖啡产品访问跟踪日志
     *
     * @param cafeTrackLogs 咖啡产品访问跟踪日志
     * @return 结果
     */
    int insertBatchIgnoreCafeTrackLog(List<CafeTrackLog> cafeTrackLogs);

    /**
     * 修改咖啡产品访问跟踪日志
     *
     * @param cafeTrackLog 咖啡产品访问跟踪日志
     * @return 结果
     */
    int updateCafeTrackLog(CafeTrackLog cafeTrackLog);

    /**
     * 批量修改咖啡产品访问跟踪日志
     *
     * @param cafeTrackLogs 咖啡产品访问跟踪日志
     * @return 结果
     */
    int updateBatchCafeTrackLog(List<CafeTrackLog> cafeTrackLogs);

    /**
     * 删除咖啡产品访问跟踪日志
     *
     * @param id 咖啡产品访问跟踪日志主键
     * @return 结果
     */
    int deleteCafeTrackLogById(Long id);

    /**
     * 批量删除咖啡产品访问跟踪日志
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeTrackLogByIds(Long[] ids);
}

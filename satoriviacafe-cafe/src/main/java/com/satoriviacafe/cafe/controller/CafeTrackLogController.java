package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeTrackLog;
import com.satoriviacafe.cafe.service.ICafeTrackLogService;
import com.satoriviacafe.common.annotation.Log;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.core.page.TableDataInfo;
import com.satoriviacafe.common.enums.BusinessType;
import com.satoriviacafe.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 咖啡产品访问跟踪日志Controller
 *
 * @author satoriviacafe
 * @since 2026-04-29
 */
@RestController
@RequestMapping("/cafe/track_log")
@RequiredArgsConstructor
public class CafeTrackLogController extends BaseController {

    private final ICafeTrackLogService cafeTrackLogService;

    /**
     * 查询咖啡产品访问跟踪日志列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeTrackLog cafeTrackLog) {
        startPage();
        List<CafeTrackLog> list = cafeTrackLogService.selectCafeTrackLogList(cafeTrackLog);
        return getDataTable(list);
    }

    /**
     * 导出咖啡产品访问跟踪日志列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:export')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeTrackLog cafeTrackLog) {
        List<CafeTrackLog> list = cafeTrackLogService.selectCafeTrackLogList(cafeTrackLog);
        ExcelUtil<CafeTrackLog> util = new ExcelUtil<>(CafeTrackLog.class);
        util.exportExcel(response, list, "咖啡产品访问跟踪日志数据");
    }

    /**
     * 获取咖啡产品访问跟踪日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(cafeTrackLogService.selectCafeTrackLogById(id));
    }

    /**
     * 新增咖啡产品访问跟踪日志
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:add')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeTrackLog cafeTrackLog) {
        return toAjax(cafeTrackLogService.insertCafeTrackLog(cafeTrackLog));
    }


    /**
     * 新增咖啡产品访问跟踪日志
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:addBatch')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeTrackLog> cafeTrackLogs) {
        return toAjax(cafeTrackLogService.insertBatchCafeTrackLog(cafeTrackLogs));
    }


    /**
     * 修改咖啡产品访问跟踪日志
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:edit')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeTrackLog cafeTrackLog) {
        return toAjax(cafeTrackLogService.updateCafeTrackLog(cafeTrackLog));
    }

    /**
     * 修改咖啡产品访问跟踪日志
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:editBatch')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeTrackLog> cafeTrackLogs) {
        return toAjax(cafeTrackLogService.updateBatchCafeTrackLog(cafeTrackLogs));
    }

    /**
     * 删除咖啡产品访问跟踪日志
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:remove')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cafeTrackLogService.deleteCafeTrackLogByIds(ids));
    }

    /**
     * 删除咖啡产品访问跟踪日志根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:track_log:remove')")
    @Log(title = "咖啡产品访问跟踪日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(cafeTrackLogService.deleteCafeTrackLogById(id));
    }
}

package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProductLine;
import com.satoriviacafe.cafe.service.ICafeProductLineService;
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
 * 咖啡产品线Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/line")
@RequiredArgsConstructor
public class CafeProductLineController extends BaseController {

    private final ICafeProductLineService cafeProductLineService;

    /**
     * 查询咖啡产品线列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProductLine cafeProductLine) {
        startPage();
        List<CafeProductLine> list = cafeProductLineService.selectCafeProductLineList(cafeProductLine);
        return getDataTable(list);
    }

    /**
     * 导出咖啡产品线列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:export')")
    @Log(title = "咖啡产品线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProductLine cafeProductLine) {
        List<CafeProductLine> list = cafeProductLineService.selectCafeProductLineList(cafeProductLine);
        ExcelUtil<CafeProductLine> util = new ExcelUtil<>(CafeProductLine.class);
        util.exportExcel(response, list, "咖啡产品线数据");
    }

    /**
     * 获取咖啡产品线详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:query')")
    @GetMapping(value = "/{lineId}")
    public AjaxResult getInfo(@PathVariable("lineId") Long lineId) {
        return success(cafeProductLineService.selectCafeProductLineByLineId(lineId));
    }

    /**
     * 新增咖啡产品线
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:add')")
    @Log(title = "咖啡产品线", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProductLine cafeProductLine) {
        return toAjax(cafeProductLineService.insertCafeProductLine(cafeProductLine));
    }


    /**
     * 新增咖啡产品线
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:addBatch')")
    @Log(title = "咖啡产品线", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProductLine> cafeProductLines) {
        return toAjax(cafeProductLineService.insertBatchCafeProductLine(cafeProductLines));
    }


    /**
     * 修改咖啡产品线
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:edit')")
    @Log(title = "咖啡产品线", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProductLine cafeProductLine) {
        return toAjax(cafeProductLineService.updateCafeProductLine(cafeProductLine));
    }

    /**
     * 修改咖啡产品线
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:editBatch')")
    @Log(title = "咖啡产品线", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProductLine> cafeProductLines) {
        return toAjax(cafeProductLineService.updateBatchCafeProductLine(cafeProductLines));
    }

    /**
     * 删除咖啡产品线
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:remove')")
    @Log(title = "咖啡产品线", businessType = BusinessType.DELETE)
    @DeleteMapping("/{lineIds}")
    public AjaxResult remove(@PathVariable Long[] lineIds) {
        return toAjax(cafeProductLineService.deleteCafeProductLineByLineIds(lineIds));
    }

    /**
     * 删除咖啡产品线根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:line:remove')")
    @Log(title = "咖啡产品线", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{lineId}")
    public AjaxResult remove(@PathVariable Long lineId) {
        return toAjax(cafeProductLineService.deleteCafeProductLineByLineId(lineId));
    }
}

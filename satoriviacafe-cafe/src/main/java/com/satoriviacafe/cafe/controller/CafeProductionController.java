package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProduction;
import com.satoriviacafe.cafe.service.ICafeProductionService;
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
 * 产品Controller
 *
 * @author satoriviacafe
 * @since 2026-04-21
 */
@RestController
@RequestMapping("/cafe/production")
@RequiredArgsConstructor
public class CafeProductionController extends BaseController {

    private final ICafeProductionService cafeProductionService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProduction cafeProduction) {
        startPage();
        List<CafeProduction> list = cafeProductionService.selectCafeProductionList(cafeProduction);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProduction cafeProduction) {
        List<CafeProduction> list = cafeProductionService.selectCafeProductionList(cafeProduction);
        ExcelUtil<CafeProduction> util = new ExcelUtil<>(CafeProduction.class);
        util.exportExcel(response, list, "产品数据");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:query')")
    @GetMapping(value = "/{prodId}")
    public AjaxResult getInfo(@PathVariable("prodId") Long prodId) {
        return success(cafeProductionService.selectCafeProductionByProdId(prodId));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProduction cafeProduction) {
        return toAjax(cafeProductionService.insertCafeProduction(cafeProduction));
    }


    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:addBatch')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProduction> cafeProductions) {
        return toAjax(cafeProductionService.insertBatchCafeProduction(cafeProductions));
    }


    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProduction cafeProduction) {
        return toAjax(cafeProductionService.updateCafeProduction(cafeProduction));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:editBatch')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProduction> cafeProductions) {
        return toAjax(cafeProductionService.updateBatchCafeProduction(cafeProductions));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{prodIds}")
    public AjaxResult remove(@PathVariable Long[] prodIds) {
        return toAjax(cafeProductionService.deleteCafeProductionByProdIds(prodIds));
    }

    /**
     * 删除产品根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:production:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{prodId}")
    public AjaxResult remove(@PathVariable Long prodId) {
        return toAjax(cafeProductionService.deleteCafeProductionByProdId(prodId));
    }
}

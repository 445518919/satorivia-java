package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProductBrew;
import com.satoriviacafe.cafe.service.ICafeProductBrewService;
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
 * 咖啡商品冲煮方案Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/brew")
@RequiredArgsConstructor
public class CafeProductBrewController extends BaseController {

    private final ICafeProductBrewService cafeProductBrewService;

    /**
     * 查询咖啡商品冲煮方案列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProductBrew cafeProductBrew) {
        startPage();
        List<CafeProductBrew> list = cafeProductBrewService.selectCafeProductBrewList(cafeProductBrew);
        return getDataTable(list);
    }

    /**
     * 导出咖啡商品冲煮方案列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:export')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProductBrew cafeProductBrew) {
        List<CafeProductBrew> list = cafeProductBrewService.selectCafeProductBrewList(cafeProductBrew);
        ExcelUtil<CafeProductBrew> util = new ExcelUtil<>(CafeProductBrew.class);
        util.exportExcel(response, list, "咖啡商品冲煮方案数据");
    }

    /**
     * 获取咖啡商品冲煮方案详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:query')")
    @GetMapping(value = "/{brewId}")
    public AjaxResult getInfo(@PathVariable("brewId") Long brewId) {
        return success(cafeProductBrewService.selectCafeProductBrewByBrewId(brewId));
    }

    /**
     * 新增咖啡商品冲煮方案
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:add')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProductBrew cafeProductBrew) {
        return toAjax(cafeProductBrewService.insertCafeProductBrew(cafeProductBrew));
    }


    /**
     * 新增咖啡商品冲煮方案
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:addBatch')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProductBrew> cafeProductBrews) {
        return toAjax(cafeProductBrewService.insertBatchCafeProductBrew(cafeProductBrews));
    }


    /**
     * 修改咖啡商品冲煮方案
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:edit')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProductBrew cafeProductBrew) {
        return toAjax(cafeProductBrewService.updateCafeProductBrew(cafeProductBrew));
    }

    /**
     * 修改咖啡商品冲煮方案
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:editBatch')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProductBrew> cafeProductBrews) {
        return toAjax(cafeProductBrewService.updateBatchCafeProductBrew(cafeProductBrews));
    }

    /**
     * 删除咖啡商品冲煮方案
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:remove')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{brewIds}")
    public AjaxResult remove(@PathVariable Long[] brewIds) {
        return toAjax(cafeProductBrewService.deleteCafeProductBrewByBrewIds(brewIds));
    }

    /**
     * 删除咖啡商品冲煮方案根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:remove')")
    @Log(title = "咖啡商品冲煮方案", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{brewId}")
    public AjaxResult remove(@PathVariable Long brewId) {
        return toAjax(cafeProductBrewService.deleteCafeProductBrewByBrewId(brewId));
    }
}

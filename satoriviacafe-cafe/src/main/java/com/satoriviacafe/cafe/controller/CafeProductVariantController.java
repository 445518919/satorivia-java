package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProductVariant;
import com.satoriviacafe.cafe.service.ICafeProductVariantService;
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
 * 咖啡商品规格Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/variant")
@RequiredArgsConstructor
public class CafeProductVariantController extends BaseController {

    private final ICafeProductVariantService cafeProductVariantService;

    /**
     * 查询咖啡商品规格列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProductVariant cafeProductVariant) {
        startPage();
        List<CafeProductVariant> list = cafeProductVariantService.selectCafeProductVariantList(cafeProductVariant);
        return getDataTable(list);
    }

    /**
     * 导出咖啡商品规格列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:export')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProductVariant cafeProductVariant) {
        List<CafeProductVariant> list = cafeProductVariantService.selectCafeProductVariantList(cafeProductVariant);
        ExcelUtil<CafeProductVariant> util = new ExcelUtil<>(CafeProductVariant.class);
        util.exportExcel(response, list, "咖啡商品规格数据");
    }

    /**
     * 获取咖啡商品规格详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:query')")
    @GetMapping(value = "/{variantId}")
    public AjaxResult getInfo(@PathVariable("variantId") Long variantId) {
        return success(cafeProductVariantService.selectCafeProductVariantByVariantId(variantId));
    }

    /**
     * 新增咖啡商品规格
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:add')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProductVariant cafeProductVariant) {
        return toAjax(cafeProductVariantService.insertCafeProductVariant(cafeProductVariant));
    }


    /**
     * 新增咖啡商品规格
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:addBatch')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProductVariant> cafeProductVariants) {
        return toAjax(cafeProductVariantService.insertBatchCafeProductVariant(cafeProductVariants));
    }


    /**
     * 修改咖啡商品规格
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:edit')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProductVariant cafeProductVariant) {
        return toAjax(cafeProductVariantService.updateCafeProductVariant(cafeProductVariant));
    }

    /**
     * 修改咖啡商品规格
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:editBatch')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProductVariant> cafeProductVariants) {
        return toAjax(cafeProductVariantService.updateBatchCafeProductVariant(cafeProductVariants));
    }

    /**
     * 删除咖啡商品规格
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:remove')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{variantIds}")
    public AjaxResult remove(@PathVariable Long[] variantIds) {
        return toAjax(cafeProductVariantService.deleteCafeProductVariantByVariantIds(variantIds));
    }

    /**
     * 删除咖啡商品规格根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:variant:remove')")
    @Log(title = "咖啡商品规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{variantId}")
    public AjaxResult remove(@PathVariable Long variantId) {
        return toAjax(cafeProductVariantService.deleteCafeProductVariantByVariantId(variantId));
    }
}

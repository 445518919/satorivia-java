package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProduct;
import com.satoriviacafe.cafe.service.ICafeProductService;
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
 * 咖啡商品主Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/product")
@RequiredArgsConstructor
public class CafeProductController extends BaseController {

    private final ICafeProductService cafeProductService;

    /**
     * 查询咖啡商品主列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProduct cafeProduct) {
        startPage();
        List<CafeProduct> list = cafeProductService.selectCafeProductList(cafeProduct);
        return getDataTable(list);
    }

    /**
     * 导出咖啡商品主列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:export')")
    @Log(title = "咖啡商品主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProduct cafeProduct) {
        List<CafeProduct> list = cafeProductService.selectCafeProductList(cafeProduct);
        ExcelUtil<CafeProduct> util = new ExcelUtil<>(CafeProduct.class);
        util.exportExcel(response, list, "咖啡商品主数据");
    }

    /**
     * 获取咖啡商品主详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:query')")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable("productId") Long productId) {
        return success(cafeProductService.selectCafeProductByProductId(productId));
    }

    /**
     * 新增咖啡商品主
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:add')")
    @Log(title = "咖啡商品主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProduct cafeProduct) {
        return toAjax(cafeProductService.insertCafeProduct(cafeProduct));
    }


    /**
     * 新增咖啡商品主
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:addBatch')")
    @Log(title = "咖啡商品主", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProduct> cafeProducts) {
        return toAjax(cafeProductService.insertBatchCafeProduct(cafeProducts));
    }


    /**
     * 修改咖啡商品主
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:edit')")
    @Log(title = "咖啡商品主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProduct cafeProduct) {
        return toAjax(cafeProductService.updateCafeProduct(cafeProduct));
    }

    /**
     * 修改咖啡商品主
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:editBatch')")
    @Log(title = "咖啡商品主", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProduct> cafeProducts) {
        return toAjax(cafeProductService.updateBatchCafeProduct(cafeProducts));
    }

    /**
     * 删除咖啡商品主
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:remove')")
    @Log(title = "咖啡商品主", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds) {
        return toAjax(cafeProductService.deleteCafeProductByProductIds(productIds));
    }

    /**
     * 删除咖啡商品主根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:product:remove')")
    @Log(title = "咖啡商品主", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{productId}")
    public AjaxResult remove(@PathVariable Long productId) {
        return toAjax(cafeProductService.deleteCafeProductByProductId(productId));
    }
}

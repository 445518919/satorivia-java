package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProductImage;
import com.satoriviacafe.cafe.service.ICafeProductImageService;
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
 * 咖啡商品图库Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/image")
@RequiredArgsConstructor
public class CafeProductImageController extends BaseController {

    private final ICafeProductImageService cafeProductImageService;

    /**
     * 查询咖啡商品图库列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProductImage cafeProductImage) {
        startPage();
        List<CafeProductImage> list = cafeProductImageService.selectCafeProductImageList(cafeProductImage);
        return getDataTable(list);
    }

    /**
     * 导出咖啡商品图库列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:export')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProductImage cafeProductImage) {
        List<CafeProductImage> list = cafeProductImageService.selectCafeProductImageList(cafeProductImage);
        ExcelUtil<CafeProductImage> util = new ExcelUtil<>(CafeProductImage.class);
        util.exportExcel(response, list, "咖啡商品图库数据");
    }

    /**
     * 获取咖啡商品图库详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:query')")
    @GetMapping(value = "/{imageId}")
    public AjaxResult getInfo(@PathVariable("imageId") Long imageId) {
        return success(cafeProductImageService.selectCafeProductImageByImageId(imageId));
    }

    /**
     * 新增咖啡商品图库
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:add')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProductImage cafeProductImage) {
        return toAjax(cafeProductImageService.insertCafeProductImage(cafeProductImage));
    }


    /**
     * 新增咖啡商品图库
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:addBatch')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProductImage> cafeProductImages) {
        return toAjax(cafeProductImageService.insertBatchCafeProductImage(cafeProductImages));
    }


    /**
     * 修改咖啡商品图库
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:edit')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProductImage cafeProductImage) {
        return toAjax(cafeProductImageService.updateCafeProductImage(cafeProductImage));
    }

    /**
     * 修改咖啡商品图库
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:editBatch')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProductImage> cafeProductImages) {
        return toAjax(cafeProductImageService.updateBatchCafeProductImage(cafeProductImages));
    }

    /**
     * 删除咖啡商品图库
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:remove')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{imageIds}")
    public AjaxResult remove(@PathVariable Long[] imageIds) {
        return toAjax(cafeProductImageService.deleteCafeProductImageByImageIds(imageIds));
    }

    /**
     * 删除咖啡商品图库根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:image:remove')")
    @Log(title = "咖啡商品图库", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{imageId}")
    public AjaxResult remove(@PathVariable Long imageId) {
        return toAjax(cafeProductImageService.deleteCafeProductImageByImageId(imageId));
    }
}

package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeBanner;
import com.satoriviacafe.cafe.service.ICafeBannerService;
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
 * 轮播图Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/banner")
@RequiredArgsConstructor
public class CafeBannerController extends BaseController {

    private final ICafeBannerService cafeBannerService;

    /**
     * 查询轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeBanner cafeBanner) {
        startPage();
        List<CafeBanner> list = cafeBannerService.selectCafeBannerList(cafeBanner);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:export')")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeBanner cafeBanner) {
        List<CafeBanner> list = cafeBannerService.selectCafeBannerList(cafeBanner);
        ExcelUtil<CafeBanner> util = new ExcelUtil<>(CafeBanner.class);
        util.exportExcel(response, list, "轮播图数据");
    }

    /**
     * 获取轮播图详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:query')")
    @GetMapping(value = "/{bannerId}")
    public AjaxResult getInfo(@PathVariable("bannerId") Long bannerId) {
        return success(cafeBannerService.selectCafeBannerByBannerId(bannerId));
    }

    /**
     * 新增轮播图
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeBanner cafeBanner) {
        return toAjax(cafeBannerService.insertCafeBanner(cafeBanner));
    }


    /**
     * 新增轮播图
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:addBatch')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeBanner> cafeBanners) {
        return toAjax(cafeBannerService.insertBatchCafeBanner(cafeBanners));
    }


    /**
     * 修改轮播图
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeBanner cafeBanner) {
        return toAjax(cafeBannerService.updateCafeBanner(cafeBanner));
    }

    /**
     * 修改轮播图
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:editBatch')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeBanner> cafeBanners) {
        return toAjax(cafeBannerService.updateBatchCafeBanner(cafeBanners));
    }

    /**
     * 删除轮播图
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bannerIds}")
    public AjaxResult remove(@PathVariable Long[] bannerIds) {
        return toAjax(cafeBannerService.deleteCafeBannerByBannerIds(bannerIds));
    }

    /**
     * 删除轮播图根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:banner:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{bannerId}")
    public AjaxResult remove(@PathVariable Long bannerId) {
        return toAjax(cafeBannerService.deleteCafeBannerByBannerId(bannerId));
    }
}

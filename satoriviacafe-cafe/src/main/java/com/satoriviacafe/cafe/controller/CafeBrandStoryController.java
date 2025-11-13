package com.satoriviacafe.cafe.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.satoriviacafe.common.annotation.Log;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.enums.BusinessType;
import com.satoriviacafe.cafe.domain.CafeBrandStory;
import com.satoriviacafe.cafe.service.ICafeBrandStoryService;
import com.satoriviacafe.common.utils.poi.ExcelUtil;
import com.satoriviacafe.common.core.page.TableDataInfo;

/**
 * 品牌故事Controller
 *
 * @author satoriviacafe
 * @since 2025-11-13
 */
@RestController
@RequestMapping("/cafe/story")
@RequiredArgsConstructor
public class CafeBrandStoryController extends BaseController {

    private final ICafeBrandStoryService cafeBrandStoryService;

    /**
     * 查询品牌故事列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeBrandStory cafeBrandStory) {
        startPage();
        List<CafeBrandStory> list = cafeBrandStoryService.selectCafeBrandStoryList(cafeBrandStory);
        return getDataTable(list);
    }

    /**
     * 导出品牌故事列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:export')")
    @Log(title = "品牌故事", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeBrandStory cafeBrandStory) {
        List<CafeBrandStory> list = cafeBrandStoryService.selectCafeBrandStoryList(cafeBrandStory);
        ExcelUtil<CafeBrandStory> util = new ExcelUtil<>(CafeBrandStory.class);
        util.exportExcel(response, list, "品牌故事数据");
    }

    /**
     * 获取品牌故事详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:query')")
    @GetMapping(value = "/{storyId}")
    public AjaxResult getInfo(@PathVariable("storyId") Long storyId) {
        return success(cafeBrandStoryService.selectCafeBrandStoryByStoryId(storyId));
    }

    /**
     * 新增品牌故事
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:add')")
    @Log(title = "品牌故事", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeBrandStory cafeBrandStory) {
        return toAjax(cafeBrandStoryService.insertCafeBrandStory(cafeBrandStory));
    }


    /**
     * 新增品牌故事
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:addBatch')")
    @Log(title = "品牌故事", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeBrandStory> cafeBrandStorys) {
        return toAjax(cafeBrandStoryService.insertBatchCafeBrandStory(cafeBrandStorys));
    }


    /**
     * 修改品牌故事
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:edit')")
    @Log(title = "品牌故事", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeBrandStory cafeBrandStory) {
        return toAjax(cafeBrandStoryService.updateCafeBrandStory(cafeBrandStory));
    }

    /**
     * 修改品牌故事
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:editBatch')")
    @Log(title = "品牌故事", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeBrandStory> cafeBrandStorys) {
        return toAjax(cafeBrandStoryService.updateBatchCafeBrandStory(cafeBrandStorys));
    }

    /**
     * 删除品牌故事
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:remove')")
    @Log(title = "品牌故事", businessType = BusinessType.DELETE)
	@DeleteMapping("/{storyIds}")
    public AjaxResult remove(@PathVariable Long[] storyIds) {
        return toAjax(cafeBrandStoryService.deleteCafeBrandStoryByStoryIds(storyIds));
    }

    /**
     * 删除品牌故事根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:story:remove')")
    @Log(title = "品牌故事", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{storyId}")
    public AjaxResult remove(@PathVariable Long storyId) {
        return toAjax(cafeBrandStoryService.deleteCafeBrandStoryByStoryId(storyId));
    }
}

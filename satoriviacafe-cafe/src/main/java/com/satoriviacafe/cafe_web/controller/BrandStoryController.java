package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeBrandStory;
import com.satoriviacafe.cafe.service.ICafeBrandStoryService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌故事Controller
 *
 * @author satoriviacafe
 * @since 2025-11-13
 */
@RestController
@RequestMapping("/v1/cafe/story")
@RequiredArgsConstructor
public class BrandStoryController extends BaseController {

    private final ICafeBrandStoryService cafeBrandStoryService;

    /**
     * 查询品牌故事列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CafeBrandStory cafeBrandStory) {
        startPage();
        List<CafeBrandStory> list = cafeBrandStoryService.selectCafeBrandStoryList(cafeBrandStory);
        return getDataTable(list);
    }

    /**
     * 获取品牌故事详细信息
     */
    @GetMapping(value = "/{storyId}")
    public AjaxResult getInfo(@PathVariable("storyId") Long storyId) {
        return success(cafeBrandStoryService.selectCafeBrandStoryByStoryId(storyId));
    }

}

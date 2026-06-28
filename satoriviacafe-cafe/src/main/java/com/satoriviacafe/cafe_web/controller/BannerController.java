package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeBanner;
import com.satoriviacafe.cafe.service.ICafeBannerService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.R;
import com.satoriviacafe.common.core.page.RT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 轮播图端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/banner")
@RequiredArgsConstructor
public class BannerController extends BaseController {

    private final ICafeBannerService cafeBannerService;

    /**
     * 查询轮播图列表
     */
    @GetMapping("/list")
    public RT<CafeBanner> list(CafeBanner cafeBanner) {
        startPage();
        List<CafeBanner> list = cafeBannerService.selectCafeBannerList(cafeBanner);
        return RT.success(list);
    }

    /**
     * 获取轮播图详细信息
     */
    @GetMapping(value = "/{bannerId}")
    public R<CafeBanner> getInfo(@PathVariable("bannerId") Long bannerId) {
        return R.ok(cafeBannerService.selectCafeBannerByBannerId(bannerId));
    }

}

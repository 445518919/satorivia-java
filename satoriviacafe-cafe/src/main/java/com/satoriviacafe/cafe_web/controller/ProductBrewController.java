package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProductBrew;
import com.satoriviacafe.cafe.service.ICafeProductBrewService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.R;
import com.satoriviacafe.common.core.page.RT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 咖啡商品冲煮方案端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/brew")
@RequiredArgsConstructor
public class ProductBrewController extends BaseController {

    private final ICafeProductBrewService cafeProductBrewService;

    /**
     * 查询咖啡商品冲煮方案列表
     */
    @GetMapping("/list")
    public RT<CafeProductBrew> list(CafeProductBrew cafeProductBrew) {
        startPage();
        List<CafeProductBrew> list = cafeProductBrewService.selectCafeProductBrewList(cafeProductBrew);
        return RT.success(list);
    }

    /**
     * 获取咖啡商品冲煮方案详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:brew:query')")
    @GetMapping(value = "/{brewId}")
    public R<CafeProductBrew> getInfo(@PathVariable("brewId") Long brewId) {
        return R.ok(cafeProductBrewService.selectCafeProductBrewByBrewId(brewId));
    }
}

package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProduction;
import com.satoriviacafe.cafe.service.ICafeProductionService;
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
 * 产品端点
 *
 * @author satoriviacafe
 * @since 2026-04-21
 */
@RestController
@RequestMapping("/v1/cafe/production")
@RequiredArgsConstructor
public class ProductionController extends BaseController {

    private final ICafeProductionService cafeProductionService;

    /**
     * 查询产品列表
     */
    @GetMapping("/list")
    public RT<CafeProduction> list(CafeProduction cafeProduction) {
        startPage();
        List<CafeProduction> list = cafeProductionService.selectCafeProductionList(cafeProduction);
        return RT.success(list);
    }

    /**
     * 获取产品详细信息
     */
    @GetMapping(value = "/{prodId}")
    public R<CafeProduction> getInfo(@PathVariable("prodId") Long prodId) {
        return R.ok(cafeProductionService.selectCafeProductionByProdId(prodId));
    }

}

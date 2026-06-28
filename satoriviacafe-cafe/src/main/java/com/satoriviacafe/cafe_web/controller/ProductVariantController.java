package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProductVariant;
import com.satoriviacafe.cafe.service.ICafeProductVariantService;
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
 * 咖啡商品规格端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/variant")
@RequiredArgsConstructor
public class ProductVariantController extends BaseController {

    private final ICafeProductVariantService cafeProductVariantService;

    /**
     * 查询咖啡商品规格列表
     */
    @GetMapping("/list")
    public RT<CafeProductVariant> list(CafeProductVariant cafeProductVariant) {
        startPage();
        List<CafeProductVariant> list = cafeProductVariantService.selectCafeProductVariantList(cafeProductVariant);
        return RT.success(list);
    }

    /**
     * 获取咖啡商品规格详细信息
     */
    @GetMapping(value = "/{variantId}")
    public R<CafeProductVariant> getInfo(@PathVariable("variantId") Long variantId) {
        return R.ok(cafeProductVariantService.selectCafeProductVariantByVariantId(variantId));
    }
}

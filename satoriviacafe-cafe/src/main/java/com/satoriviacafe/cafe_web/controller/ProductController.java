package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProduct;
import com.satoriviacafe.cafe.service.ICafeProductService;
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
 * 咖啡商品主端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/product")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ICafeProductService cafeProductService;

    /**
     * 查询咖啡商品主列表
     */
    @GetMapping("/list")
    public RT<CafeProduct> list(CafeProduct cafeProduct) {
        startPage();
        List<CafeProduct> list = cafeProductService.selectCafeProductList(cafeProduct);
        return RT.success(list);
    }

    /**
     * 获取咖啡商品主详细信息
     */
    @GetMapping(value = "/{productId}")
    public R<CafeProduct> getInfo(@PathVariable("productId") Long productId) {
        return R.ok(cafeProductService.selectCafeProductByProductId(productId));
    }

}

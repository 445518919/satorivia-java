package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProductImage;
import com.satoriviacafe.cafe.service.ICafeProductImageService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 咖啡商品图库端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/image")
@RequiredArgsConstructor
public class ProductImageController extends BaseController {

    private final ICafeProductImageService cafeProductImageService;

    /**
     * 获取咖啡商品图库详细信息
     */
    @GetMapping(value = "/{imageId}")
    public R<CafeProductImage> getInfo(@PathVariable("imageId") Long imageId) {
        return R.ok(cafeProductImageService.selectCafeProductImageByImageId(imageId));
    }

}

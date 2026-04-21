package com.satoriviacafe.chain.controller;

import com.satoriviacafe.cafe.service.ProductService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * NFC 相關控制器
 *
 * @author shy
 * @since 2026年04月21日
 */
@RestController
@RequestMapping("/chain/nfc")
@RequiredArgsConstructor
public class NfcController extends BaseController {

    private final ProductService productService;


    @GetMapping("/product/detail/{code}")
    AjaxResult detail(@PathVariable String code) {
        return success(productService.detail(code));
    }
}

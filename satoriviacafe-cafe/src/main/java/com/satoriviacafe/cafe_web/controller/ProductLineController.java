package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProductLine;
import com.satoriviacafe.cafe.service.ICafeProductLineService;
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
 * 咖啡产品线端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/line")
@RequiredArgsConstructor
public class ProductLineController extends BaseController {

    private final ICafeProductLineService cafeProductLineService;

    /**
     * 查询咖啡产品线列表
     */
    @GetMapping("/list")
    public RT<CafeProductLine> list(CafeProductLine cafeProductLine) {
        startPage();
        List<CafeProductLine> list = cafeProductLineService.selectCafeProductLineList(cafeProductLine);
        return RT.success(list);
    }

    /**
     * 获取咖啡产品线详细信息
     */
    @GetMapping(value = "/{lineId}")
    public R<CafeProductLine> getInfo(@PathVariable("lineId") Long lineId) {
        return R.ok(cafeProductLineService.selectCafeProductLineByLineId(lineId));
    }

}

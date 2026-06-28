package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.service.TrackService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 跟踪信息端点
 *
 * @author shy
 * @since 2026年04月21日
 */
@RestController
@RequestMapping("/v1/cafe/track")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrackController extends BaseController {

    private final TrackService trackService;

    @PostMapping("/{productId}/{eventName}")
    public R<Void> detail(@PathVariable Long productId, @PathVariable String eventName, HttpServletRequest request) {
        trackService.track(productId, eventName, request);
        return R.ok();
    }
}

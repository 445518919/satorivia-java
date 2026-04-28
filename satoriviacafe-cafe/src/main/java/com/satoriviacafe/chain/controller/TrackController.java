package com.satoriviacafe.chain.controller;

import com.satoriviacafe.cafe.service.TrackService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 跟踪信息控制器
 *
 * @author shy
 * @since 2026年04月21日
 */
@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrackController extends BaseController {

    private final TrackService trackService;

    @PostMapping("/{code}/{eventName}")
    AjaxResult detail(@PathVariable String code, @PathVariable String eventName, HttpServletRequest request) {
        trackService.track(code, eventName, request);
        return success();
    }
}

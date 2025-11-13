package com.satoriviacafe.web.controller.system;

import com.satoriviacafe.common.config.SatoriviacafeConfig;
import com.satoriviacafe.common.utils.VStringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author satoriviacafe
 */
@RestController
@RequiredArgsConstructor
public class SysIndexController {

    /**
     * 系统基础配置
     */
    private final SatoriviacafeConfig SatoriviacafeConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return VStringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", SatoriviacafeConfig.getName(), SatoriviacafeConfig.getVersion());
    }
}

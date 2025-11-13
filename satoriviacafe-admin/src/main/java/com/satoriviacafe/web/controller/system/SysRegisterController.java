package com.satoriviacafe.web.controller.system;

import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.core.domain.satoriviacafe.RegisterBody;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.framework.web.service.SysRegisterService;
import com.satoriviacafe.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author satoriviacafe
 */
@RestController
@RequiredArgsConstructor
public class SysRegisterController extends BaseController {

    private final SysRegisterService registerService;
    private final ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return VStringUtils.isEmpty(msg) ? success() : error(msg);
    }
}

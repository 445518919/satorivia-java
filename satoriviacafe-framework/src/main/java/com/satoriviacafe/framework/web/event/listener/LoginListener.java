package com.satoriviacafe.framework.web.event.listener;

import com.satoriviacafe.common.config.SatoriviacafeConfig;
import com.satoriviacafe.common.core.domain.entity.SysUser;
import com.satoriviacafe.common.core.domain.satoriviacafe.LoginUser;
import com.satoriviacafe.framework.web.event.LoginEvent;
import com.satoriviacafe.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author shy
 * @since 2025年05月28日
 */
@Component
@RequiredArgsConstructor
public class LoginListener {


    private final ISysUserService userService;
    private final SatoriviacafeConfig satoriviacafe;

    @EventListener
    public void onLoginSuccess(LoginEvent loginEvent) {
        LoginUser loginUser = loginEvent.getSource();
        if (ObjectUtils.isEmpty(loginUser.getUser().getRoles())) {
            // 如果用户没有角色，分配默认角色
            SysUser sysUser = userService.selectUserById(loginUser.getUserId());
            sysUser.addRoleIds(satoriviacafe.getDefaultRoleId());
            userService.updateUser(sysUser);
        }
    }
}

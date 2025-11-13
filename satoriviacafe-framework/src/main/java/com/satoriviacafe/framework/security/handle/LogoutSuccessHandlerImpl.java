package com.satoriviacafe.framework.security.handle;

import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.core.domain.satoriviacafe.LoginUser;
import com.satoriviacafe.common.utils.MessageUtils;
import com.satoriviacafe.common.utils.ServletUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.JSONUtil; // 引入JSONUtil
import com.satoriviacafe.framework.manager.AsyncManager;
import com.satoriviacafe.framework.manager.factory.AsyncFactory;
import com.satoriviacafe.framework.web.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 自定义退出处理类 返回成功（Jackson实现）
 *
 * @author satoriviacafe
 */
@Configuration
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final AsyncManager asyncManager;
    private final TokenService tokenService;

    /**
     * 退出处理
     *
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (VStringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            asyncManager.executeDelay(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        // 核心替换：用JSONUtil序列化响应结果，替代FastJSON
        ServletUtils.renderString(response, JSONUtil.toJsonString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}

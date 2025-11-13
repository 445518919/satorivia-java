package com.satoriviacafe.framework.interceptor;

import com.satoriviacafe.common.annotation.RepeatSubmit;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.utils.ServletUtils;
import com.satoriviacafe.common.utils.JSONUtil; // 引入自定义JSONUtil，替代FastJSON
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器（Jackson实现，基于JSONUtil）
 *
 * @author satoriviacafe
 */
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation)) {
                    // 核心替换：用JSONUtil序列化AjaxResult，替代FastJSON的JSON.toJSONString
                    AjaxResult ajaxResult = AjaxResult.error(annotation.message());
                    String jsonResult = JSONUtil.toJsonString(ajaxResult); // 兼容null值，与FastJSON行为一致
                    ServletUtils.renderString(response, jsonResult);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体地防重复提交的规则
     *
     * @param request    请求信息
     * @param annotation 防重复注解参数
     * @return 结果
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);
}

package com.satoriviacafe.framework.aspectj;

import com.satoriviacafe.common.annotation.Log;
import com.satoriviacafe.common.core.domain.entity.SysUser;
import com.satoriviacafe.common.core.domain.satoriviacafe.LoginUser;
import com.satoriviacafe.common.core.text.Convert;
import com.satoriviacafe.common.enums.BusinessStatus;
import com.satoriviacafe.common.enums.HttpMethod;
import com.satoriviacafe.common.filter.PropertyPreExcludeFilter;
import com.satoriviacafe.common.utils.ExceptionUtil;
import com.satoriviacafe.common.utils.JSONUtil;
import com.satoriviacafe.common.utils.SecurityUtils;
import com.satoriviacafe.common.utils.ServletUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.ip.IpUtils;
import com.satoriviacafe.framework.manager.AsyncManager;
import com.satoriviacafe.framework.manager.factory.AsyncFactory;
import com.satoriviacafe.system.domain.SysOperLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理（完全依赖 JSONUtil 实现 JSON 序列化）
 *
 * @author satoriviacafe
 */
@SuppressWarnings({"unused"})
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private static final String LOG_FILTER = "logFilter";

    /**
     * 计算操作消耗时间（虚拟线程安全）
     */
    private static final ScopedValue<Long> COST_TIME = ScopedValue.newInstance();

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    // 1. 删除自定义的 ObjectMapper 实例（完全依赖 JSONUtil）

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};
    private final AsyncManager asyncManager;

    // -------------------------- 原有生命周期方法（不变） --------------------------
    @Before(value = "@annotation(controllerLog)")
    public void doBefore(JoinPoint joinPoint, Log controllerLog) {
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        ScopedValue.where(COST_TIME, System.currentTimeMillis()).run(() -> handleLog(joinPoint, controllerLog, null, jsonResult));
    }

    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        ScopedValue.where(COST_TIME, System.currentTimeMillis()).run(() -> handleLog(joinPoint, controllerLog, e, null));
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 原有日志组装逻辑（不变）
            LoginUser loginUser = SecurityUtils.getLoginUser();
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            operLog.setOperUrl(VStringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            if (loginUser != null) {
                operLog.setOperName(loginUser.getUsername());
                SysUser currentUser = loginUser.getUser();
                if (VStringUtils.isNotNull(currentUser) && VStringUtils.isNotNull(currentUser.getDept())) {
                    operLog.setDeptName(currentUser.getDept().getDeptName());
                }
            }
            if (e != null) {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(VStringUtils.substring(Convert.toStr(e.getMessage(), ExceptionUtil.getExceptionMessage(e)), 0, 2000));
            }
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod("%s.%s()".formatted(className, methodName));
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());

            // 处理注解参数（核心改造：调用 JSONUtil）
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);

            operLog.setCostTime(System.currentTimeMillis() - COST_TIME.get());
            asyncManager.executeDelay(AsyncFactory.recordOper(operLog));
        } catch (Exception exp) {
            log.error("异常信息:{},{}", exp.getMessage(), exp.getStackTrace());
        }
    }

    /**
     * 2. 改造：响应结果序列化（调用 JSONUtil.toJsonString）
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log opLog, SysOperLog operLog, Object jsonResult) {
        operLog.setBusinessType(opLog.businessType().ordinal());
        operLog.setTitle(opLog.title());
        operLog.setOperatorType(opLog.operatorType().ordinal());

        if (opLog.isSaveRequestData()) {
            setRequestValue(joinPoint, operLog, opLog.excludeParamNames());
        }

        // 响应结果序列化：用 JSONUtil 替代 ObjectMapper
        if (opLog.isSaveResponseData() && VStringUtils.isNotNull(jsonResult)) {
            try {
                String jsonResultStr = JSONUtil.toJsonString(jsonResult);
                operLog.setJsonResult(VStringUtils.substring(jsonResultStr, 0, 2000));
            } catch (RuntimeException e) {
                log.error("JSONUtil 序列化响应结果失败", e);
                operLog.setJsonResult("[序列化失败]");
            }
        }
    }

    /**
     * 3. 改造：请求参数序列化（调用 JSONUtil.toJsonStringWithFilter）
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames) {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();

        if (VStringUtils.isEmpty(paramsMap) && VStringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(VStringUtils.substring(params, 0, 2000));
        } else {
            try {
                // 敏感字段过滤：调用 JSONUtil 带过滤器的方法
                PropertyPreExcludeFilter filter = excludePropertyPreFilter(excludeParamNames);
                String paramsStr = JSONUtil.toJsonStringWithFilter(paramsMap, LOG_FILTER, filter);
                operLog.setOperParam(VStringUtils.substring(paramsStr, 0, 2000));
            } catch (RuntimeException e) {
                log.error("JSONUtil 序列化请求参数失败", e);
                operLog.setOperParam("[序列化失败]");
            }
        }
    }

    /**
     * 3. 改造：参数数组拼装（调用 JSONUtil.toJsonStringWithFilter）
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            PropertyPreExcludeFilter filter = excludePropertyPreFilter(excludeParamNames);
            for (Object o : paramsArray) {
                if (VStringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        // 敏感字段过滤：调用 JSONUtil 带过滤器的方法
                        String jsonObj = JSONUtil.toJsonStringWithFilter(o, LOG_FILTER,filter);
                        params.append(jsonObj).append(" ");
                    } catch (RuntimeException ignored) {
                        // 忽略单个参数序列化失败（避免整体日志中断）
                    }
                }
            }
        }
        return params.toString().trim();
    }

    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames) {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}

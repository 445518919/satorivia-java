package com.satoriviacafe.framework.manager.factory;

import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.utils.LogUtils;
import com.satoriviacafe.common.utils.ServletUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.ip.AddressUtils;
import com.satoriviacafe.common.utils.ip.IpUtils;
import com.satoriviacafe.common.utils.spring.SpringUtils;
import com.satoriviacafe.system.domain.SysLogininfor;
import com.satoriviacafe.system.domain.SysOperLog;
import com.satoriviacafe.system.service.ISysLogininforService;
import com.satoriviacafe.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;


/**
 * 异步工厂（产生任务用）
 *
 * @author satoriviacafe
 */
@Slf4j
public class AsyncFactory {

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     参数
     * @return Runnable 任务
     */
    public static Runnable recordLogininfor(final String username, final String status, final String message,
                                            final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();


        return () -> {
            String address = AddressUtils.getRealAddressByIP(ip);
            String logMsg = LogUtils.getBlock(ip)
                    + address
                    + LogUtils.getBlock(username)
                    + LogUtils.getBlock(status)
                    + LogUtils.getBlock(message);
            // 打印信息到日志
            log.info(logMsg, args);

            // 获取客户端操作系统和浏览器
            String os = userAgent.getOperatingSystem().getName();
            String browser = userAgent.getBrowser().getName();

            // 封装对象
            SysLogininfor logininfor = new SysLogininfor();
            logininfor.setUserName(username);
            logininfor.setIpaddr(ip);
            logininfor.setLoginLocation(address);
            logininfor.setBrowser(browser);
            logininfor.setOs(os);
            logininfor.setMsg(message);

            // 日志状态
            if (VStringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                logininfor.setStatus(Constants.SUCCESS);
            } else if (Constants.LOGIN_FAIL.equals(status)) {
                logininfor.setStatus(Constants.FAIL);
            }
            // 插入数据
            SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return Runnable 任务
     */
    public static Runnable recordOper(final SysOperLog operLog) {
        return () -> {
            // 远程查询操作地点
            operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
            SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
        };
    }
}

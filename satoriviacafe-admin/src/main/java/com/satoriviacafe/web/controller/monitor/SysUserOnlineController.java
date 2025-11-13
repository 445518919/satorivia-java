package com.satoriviacafe.web.controller.monitor;

import com.satoriviacafe.common.annotation.Log;
import com.satoriviacafe.common.constant.CacheConstants;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.core.domain.satoriviacafe.LoginUser;
import com.satoriviacafe.common.core.page.TableDataInfo;
import com.satoriviacafe.common.core.redis.RedisCache;
import com.satoriviacafe.common.enums.BusinessType;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.system.domain.SysUserOnline;
import com.satoriviacafe.system.service.ISysUserOnlineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 在线用户监控
 *
 * @author satoriviacafe
 */
@RestController
@RequestMapping("/monitor/online")
@RequiredArgsConstructor
@Slf4j
public class SysUserOnlineController extends BaseController {

    private final ISysUserOnlineService userOnlineService;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;

    /**
     * 查询在线用户列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName) {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<>();

        for (String key : keys) {
            try {
                // 从 Redis 获取缓存对象
                Object cacheObject = redisCache.getCacheObject(key);
                LoginUser user = null;

                if (cacheObject instanceof LoginUser) {
                    // 场景1：缓存直接是 LoginUser 实例
                    user = (LoginUser) cacheObject;
                } else if (cacheObject instanceof String jsonStr) {
                    // 场景2：缓存是 JSON 字符串，反序列化为 LoginUser
                    user = objectMapper.readValue(jsonStr, LoginUser.class);
                } else if (cacheObject != null) {
                    // 场景3：缓存是 Map/JSONObject 等，直接转为 LoginUser（无需中间字符串）
                    user = objectMapper.convertValue(cacheObject, LoginUser.class);
                }

                // 根据筛选条件添加在线用户信息
                if (user != null) {
                    if (VStringUtils.isNotEmpty(ipaddr) && VStringUtils.isNotEmpty(userName)) {
                        userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
                    } else if (VStringUtils.isNotEmpty(ipaddr)) {
                        userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
                    } else if (VStringUtils.isNotEmpty(userName) && VStringUtils.isNotNull(user.getUser())) {
                        userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
                    } else {
                        userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
                    }
                } else {
                    log.warn("缓存解析失败，删除无效缓存：{}", key);
                    redisCache.deleteObject(key);
                }
            } catch (JsonProcessingException e) {
                // 捕获 Jackson 反序列化异常（如 JSON 格式错误）
                log.error("JSON 反序列化失败，缓存键：{}", key, e);
                redisCache.deleteObject(key);
            } catch (Exception e) {
                // 捕获其他未知异常（如类型不匹配、空指针等）
                log.error("处理在线用户数据异常，缓存键：{}", key, e);
                redisCache.deleteObject(key);
            }
        }

        // 反转列表（按登录时间倒序）并移除 null 元素
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强制退出在线用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }
}

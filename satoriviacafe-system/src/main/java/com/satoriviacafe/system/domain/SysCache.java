package com.satoriviacafe.system.domain;

import com.satoriviacafe.common.utils.VStringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存信息
 *
 * @author satoriviacafe
 */
@Data
@NoArgsConstructor
public class SysCache
{
    /** 缓存名称 */
    private String cacheName = "";

    /** 缓存键名 */
    private String cacheKey = "";

    /** 缓存内容 */
    private String cacheValue = "";

    /** 备注 */
    private String remark = "";

    public SysCache(String cacheName, String remark)
    {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue)
    {
        this.cacheName = VStringUtils.replace(cacheName, ":", "");
        this.cacheKey = VStringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }

}

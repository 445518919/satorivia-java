package com.satoriviacafe.cafe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 咖啡产品访问跟踪日志对象 cafe_track_log
 *
 * @author satoriviacafe
 * @since 2026-04-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeTrackLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 产品ID
     */
    @Excel(name = "产品ID")
    private String prodId;
    /**
     * 商品编码
     */
    @Excel(name = "商品编码")
    private String prodCode;
    /**
     * 产品名称 (如: 埃塞瑰夏精品咖啡豆)
     */
    @Excel(name = "产品名称 (如: 埃塞瑰夏精品咖啡豆)")
    private String prodName;
    /**
     * 事件类型 (如: page_view)
     */
    @Excel(name = "事件类型 (如: page_view)")
    private String eventName;
    /**
     * 访问者IP地址 (支持IPv6)
     */
    @Excel(name = "访问者IP地址 (支持IPv6)")
    private String ip;
    /**
     * 访问者地理位置
     */
    @Excel(name = "访问者地理位置")
    private String location;
    /**
     * 原始浏览器User-Agent字符串
     */
    @Excel(name = "原始浏览器User-Agent字符串")
    private String userAgent;
    /**
     * 设备类型 (如: Mobile, Desktop, Tablet)
     */
    @Excel(name = "设备类型 (如: Mobile, Desktop, Tablet)")
    private String deviceType;
    /**
     * 浏览器名称 (如: Chrome, WeChat, Safari)
     */
    @Excel(name = "浏览器名称 (如: Chrome, WeChat, Safari)")
    private String browser;
    /**
     * 操作系统 (如: iOS, Android, Windows)
     */
    @Excel(name = "操作系统 (如: iOS, Android, Windows)")
    private String os;
    /**
     * 当前页面URL
     */
    @Excel(name = "当前页面URL")
    private String pageUrl;
    /**
     * 来源页面URL
     */
    @Excel(name = "来源页面URL")
    private String referrerUrl;
    /**
     * 记录创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "记录创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;
}

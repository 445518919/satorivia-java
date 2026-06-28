package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 轮播图对象 cafe_banner
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeBanner extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 轮播图id
     */
    private Long bannerId;
    /**
     * 轮播图标题
     */
    @Excel(name = "轮播图标题")
    private String bannerTitle;
    /**
     * 轮播图图片
     */
    @Excel(name = "轮播图图片")
    private String bannerImg;
    /**
     * 轮播图链接
     */
    @Excel(name = "轮播图链接")
    private String bannerLink;
    /**
     * 轮播图描述
     */
    @Excel(name = "轮播图描述")
    private String bannerDesc;
    /**
     * 轮播图状态（0正常 1停用）
     */
    @Excel(name = "轮播图状态", readConverterExp = "0=正常,1=停用")
    private String bannerStatus;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

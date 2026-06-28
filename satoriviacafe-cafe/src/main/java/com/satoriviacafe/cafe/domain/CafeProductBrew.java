package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 咖啡商品冲煮方案对象 cafe_product_brew
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProductBrew extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 冲煮方案id
     */
    private Long brewId;
    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Long productId;
    /**
     * 方案名称，例如推荐参数、V60
     */
    @Excel(name = "方案名称，例如推荐参数、V60")
    private String brewName;
    /**
     * 冲煮方式/器具
     */
    @Excel(name = "冲煮方式/器具")
    private String brewMethod;
    /**
     * 粉量
     */
    @Excel(name = "粉量")
    private String powderQuantity;
    /**
     * 水量
     */
    @Excel(name = "水量")
    private String waterVolume;
    /**
     * 水温
     */
    @Excel(name = "水温")
    private String waterTemp;
    /**
     * 研磨度
     */
    @Excel(name = "研磨度")
    private String grindDegree;
    /**
     * 粉水比
     */
    @Excel(name = "粉水比")
    private String powderWaterRatio;
    /**
     * 萃取时间
     */
    @Excel(name = "萃取时间")
    private String extractionTime;
    /**
     * 目标总时间
     */
    @Excel(name = "目标总时间")
    private String targetTime;
    /**
     * 准备工作
     */
    @Excel(name = "准备工作")
    private String preparations;
    /**
     * 冲煮步骤
     */
    @Excel(name = "冲煮步骤")
    private String brewSteps;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long brewSort;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String brewStatus;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

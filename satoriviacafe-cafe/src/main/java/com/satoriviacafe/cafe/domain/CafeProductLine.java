package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 咖啡产品线对象 cafe_product_line
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProductLine extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 产品线id
     */
    private Long lineId;
    /**
     * 产品线编码
     */
    @Excel(name = "产品线编码")
    private String lineCode;
    /**
     * 系列编码
     */
    @Excel(name = "系列编码")
    private String seriesCode;
    /**
     * 产品线名称
     */
    @Excel(name = "产品线名称")
    private String lineName;
    /**
     * 产品线副标题
     */
    @Excel(name = "产品线副标题")
    private String lineSubTitle;
    /**
     * 产品线说明
     */
    @Excel(name = "产品线说明")
    private String lineDescription;
    /**
     * 产品线桌面端封面
     */
    @Excel(name = "产品线桌面端封面")
    private String coverImage;
    /**
     * 产品线手机端封面
     */
    @Excel(name = "产品线手机端封面")
    private String mobileCoverImage;
    /**
     * SEO标题
     */
    @Excel(name = "SEO标题")
    private String seoTitle;
    /**
     * SEO描述
     */
    @Excel(name = "SEO描述")
    private String seoDescription;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long lineSort;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String lineStatus;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

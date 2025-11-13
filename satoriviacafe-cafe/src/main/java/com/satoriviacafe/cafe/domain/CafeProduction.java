package com.satoriviacafe.cafe.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;

/**
 * 产品对象 cafe_production
 *
 * @author satoriviacafe
 * @since 2025-11-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProduction extends BaseEntity {
        @Serial
    private static final long serialVersionUID = 1L;

    /** 产品id */
    private Long prodId;
    /** 产品名称 */
    @Excel(name = "产品名称")
    private String prodName;
    /** 产品描述 */
    @Excel(name = "产品描述")
    private String prodDesc;
    /** 产品图片 */
    @Excel(name = "产品图片")
    private String prodImg;
    /** 产品价格 */
    @Excel(name = "产品价格")
    private BigDecimal prodPrice;
    /** 产品详情 */
    @Excel(name = "产品详情")
    private String prodText;
    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;
    /** 产品状态（0正常 1停用） */
@Excel(name = "产品状态", readConverterExp = "0=正常,1=停用")
    private String prodStatus;
    /** 删除时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
@Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deleteAt;
}

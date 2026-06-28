package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 咖啡商品规格对象 cafe_product_variant
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProductVariant extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品规格id
     */
    private Long variantId;
    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Long productId;
    /**
     * 规格名称，例如227g、454g
     */
    @Excel(name = "规格名称，例如227g、454g")
    private String variantName;
    /**
     * 外部或内部SKU编码
     */
    @Excel(name = "外部或内部SKU编码")
    private String skuCode;
    /**
     * 净含量
     */
    @Excel(name = "净含量")
    private String netContent;
    /**
     * 销售价格
     */
    @Excel(name = "销售价格")
    private BigDecimal salePrice;
    /**
     * 划线价
     */
    @Excel(name = "划线价")
    private BigDecimal marketPrice;
    /**
     * 库存状态（0有货 1缺货 2预售）
     */
    @Excel(name = "库存状态", readConverterExp = "0=有货,1=缺货,2=预售")
    private String stockStatus;
    /**
     * 该规格的外部购买链接
     */
    @Excel(name = "该规格的外部购买链接")
    private String purchaseUrl;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long variantSort;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String variantStatus;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

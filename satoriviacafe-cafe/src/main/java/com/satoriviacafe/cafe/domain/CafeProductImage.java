package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 咖啡商品图库对象 cafe_product_image
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProductImage extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品图片id
     */
    private Long imageId;
    /**
     * 商品id
     */
    @Excel(name = "商品id")
    private Long productId;
    /**
     * 图片地址
     */
    @Excel(name = "图片地址")
    private String productImage;
    /**
     * 图片替代文本
     */
    @Excel(name = "图片替代文本")
    private String imageAlt;
    /**
     * 图片类型：gallery/detail/og
     */
    @Excel(name = "图片类型：gallery/detail/og")
    private String imageType;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long imageSort;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String imageStatus;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

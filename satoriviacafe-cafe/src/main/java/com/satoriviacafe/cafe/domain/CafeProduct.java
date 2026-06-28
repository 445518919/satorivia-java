package com.satoriviacafe.cafe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 咖啡商品主对象 cafe_product
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProduct extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long productId;
    /**
     * 产品线id，关联cafe_product_line.line_id
     */
    @Excel(name = "产品线id，关联cafe_product_line.line_id")
    private Long lineId;
    /**
     * URL商品编码，对应前端id
     */
    @Excel(name = "URL商品编码，对应前端id")
    private String productSlug;
    /**
     * 商品完整名称
     */
    @Excel(name = "商品完整名称")
    private String productName;
    /**
     * 商品短名称
     */
    @Excel(name = "商品短名称")
    private String productShortName;
    /**
     * 商品标签
     */
    @Excel(name = "商品标签")
    private String productLabel;
    /**
     * 商品摘要
     */
    @Excel(name = "商品摘要")
    private String productSummary;
    /**
     * 风味特点
     */
    @Excel(name = "风味特点")
    private String flavor;
    /**
     * 产地/庄园
     */
    @Excel(name = "产地/庄园")
    private String origin;
    /**
     * 海拔高度
     */
    @Excel(name = "海拔高度")
    private String altitude;
    /**
     * 咖啡品种
     */
    @Excel(name = "咖啡品种")
    private String variety;
    /**
     * 处理方式
     */
    @Excel(name = "处理方式")
    private String treatment;
    /**
     * 烘焙度
     */
    @Excel(name = "烘焙度")
    private String roastDegree;
    /**
     * 推荐冲煮方法概述
     */
    @Excel(name = "推荐冲煮方法概述")
    private String brewingMethod;
    /**
     * 建议粉量/用量
     */
    @Excel(name = "建议粉量/用量")
    private String dosage;
    /**
     * 萃取时间
     */
    @Excel(name = "萃取时间")
    private String extractionTime;
    /**
     * 建议水温
     */
    @Excel(name = "建议水温")
    private String waterTemp;
    /**
     * 粉水比
     */
    @Excel(name = "粉水比")
    private String powderWaterRatio;
    /**
     * 规格概述
     */
    @Excel(name = "规格概述")
    private String specification;
    /**
     * 包装说明
     */
    @Excel(name = "包装说明")
    private String packageDesc;
    /**
     * 添加物说明
     */
    @Excel(name = "添加物说明")
    private String additives;
    /**
     * 商品详情正文
     */
    @Excel(name = "商品详情正文")
    private String productDescription;
    /**
     * 产品特色，建议存JSON数组或HTML
     */
    @Excel(name = "产品特色，建议存JSON数组或HTML")
    private String productFeatures;
    /**
     * 商品主图/产品线列表封面
     */
    @Excel(name = "商品主图/产品线列表封面")
    private String primaryImage;
    /**
     * 风味雷达图
     */
    @Excel(name = "风味雷达图")
    private String radarImage;
    /**
     * 默认外部购买链接
     */
    @Excel(name = "默认外部购买链接")
    private String purchaseUrl;
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
     * 社交分享图片
     */
    @Excel(name = "社交分享图片")
    private String ogImage;
    /**
     * 是否推荐（0否 1是）
     */
    @Excel(name = "是否推荐", readConverterExp = "0=否,1=是")
    private String isFeatured;
    /**
     * 产品线内排序
     */
    @Excel(name = "产品线内排序")
    private Long productSort;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String productStatus;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishTime;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

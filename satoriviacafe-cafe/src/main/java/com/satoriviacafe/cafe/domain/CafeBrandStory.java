package com.satoriviacafe.cafe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 品牌故事主对象 cafe_brand_story
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeBrandStory extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 品牌故事id
     */
    private Long storyId;
    /**
     * 唯一编码
     */
    @Excel(name = "唯一编码")
    private String storyCode;
    /**
     * 英文栏目名
     */
    @Excel(name = "英文栏目名")
    private String storyLabel;
    /**
     * 标题
     */
    @Excel(name = "标题")
    private String storyTitle;
    /**
     * 副标题
     */
    @Excel(name = "副标题")
    private String storySubTitle;
    /**
     * 品牌引言
     */
    @Excel(name = "品牌引言")
    private String storyQuote;
    /**
     * 品牌摘要
     */
    @Excel(name = "品牌摘要")
    private String storySummary;
    /**
     * 品牌故事封面
     */
    @Excel(name = "品牌故事封面")
    private String coverImage;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long storySort;
    /**
     * 发布状态
     */
    @Excel(name = "发布状态")
    private String publishStatus;
    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String storyStatus;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
@Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishTime;
    /** 删除时间 */
    private Date deleteAt;
}

package com.satoriviacafe.cafe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 品牌故事对象 cafe_brand_story
 *
 * @author satoriviacafe
 * @since 2025-11-13
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
     * 品牌故事图片
     */
    @Excel(name = "品牌故事图片")
    private String storyImg;
    /**
     * 品牌故事详情
     */
    @Excel(name = "品牌故事详情")
    private String storyText;
    /**
     * 品牌故事状态（0正常 1停用）
     */
    @Excel(name = "品牌故事状态", readConverterExp = "0=正常,1=停用")
    private String storyStatus;
    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deleteAt;
}

package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 豆子笔记对象 cafe_beans_note
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeBeansNote extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 豆子笔记id
     */
    private Long noteId;
    /**
     * 豆子笔记标题
     */
    @Excel(name = "豆子笔记标题")
    private String noteTitle;
    /**
     * 豆子笔记副标题
     */
    @Excel(name = "豆子笔记副标题")
    private String noteSubTitle;
    /**
     * 豆子笔记内容
     */
    @Excel(name = "豆子笔记内容")
    private String noteContent;
    /**
     * 豆子笔记图片
     */
    @Excel(name = "豆子笔记图片")
    private String noteImage;
    /**
     * 冲泡时间
     */
    @Excel(name = "冲泡时间")
    private String brewTime;
    /**
     * 粉量
     */
    @Excel(name = "粉量")
    private String poderQuantity;
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
     * 冲泡步骤
     */
    @Excel(name = "冲泡步骤")
    private String brewSteps;
    /**
     * 豆子笔记状态（0正常 1停用）
     */
    @Excel(name = "豆子笔记状态", readConverterExp = "0=正常,1=停用")
    private String noteStatus;
    /**
     * 删除时间
     */
    private Date deleteAt;
}

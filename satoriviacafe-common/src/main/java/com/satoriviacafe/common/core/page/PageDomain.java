package com.satoriviacafe.common.core.page;

import com.satoriviacafe.common.utils.VStringUtils;
import lombok.Data;

/**
 * 分页数据
 *
 * @author satoriviacafe
 */
@Data
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";

    /**
     * 分页参数合理化
     */
    private Boolean reasonable = true;

    public String getOrderBy() {
        if (VStringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return VStringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public void setIsAsc(String isAsc) {
        if (VStringUtils.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    public Boolean getReasonable() {
        if (VStringUtils.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }

}

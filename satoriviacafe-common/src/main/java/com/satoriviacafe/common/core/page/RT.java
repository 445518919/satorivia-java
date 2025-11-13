package com.satoriviacafe.common.core.page;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * @author shy
 * @since 2025年09月18日
 */
@Data
@NoArgsConstructor
public class RT<T> implements Serializable {
    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public RT(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 返回成功
     */
    public static <T> RT<T> success(List<T> list) {
        return new RT<>(list, new PageInfo<>(list).getTotal());
    }
}

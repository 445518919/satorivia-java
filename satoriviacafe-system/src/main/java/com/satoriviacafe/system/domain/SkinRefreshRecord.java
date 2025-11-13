package com.satoriviacafe.system.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author shy
 * @since 2025年05月05日
 */
@Data
@Accessors(chain = true)
public class SkinRefreshRecord {

    private Long id;
    private String skinId;
    private Long userId;
    private Date createTime;

    private Long count;

    private Date beginCreateTime;
    private Date endCreateTime;
}

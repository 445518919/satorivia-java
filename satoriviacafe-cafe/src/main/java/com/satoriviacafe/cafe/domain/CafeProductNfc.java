package com.satoriviacafe.cafe.domain;

import com.satoriviacafe.common.annotation.Excel;
import com.satoriviacafe.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 产品NFC对象 cafe_product_nfc
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CafeProductNfc extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 产品NFCid
     */
    private Long nfcId;
    /**
     * NFC编码
     */
    @Excel(name = "NFC编码")
    private String nfcCode;
    /**
     * SKU编码
     */
    @Excel(name = "SKU编码")
    private Long skuCode;
    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String productName;
    /**
     * 批次名称
     */
    @Excel(name = "批次名称")
    private String batchName;
    /**
     * 删除时间
     */
    private Date deleteAt;
    /**
     * NFC哈希值
     */
    @Excel(name = "NFC哈希值")
    private String nfcHash;
    /**
     * NFC链接
     */
    @Excel(name = "NFC链接")
    private String nfcUrl;
}

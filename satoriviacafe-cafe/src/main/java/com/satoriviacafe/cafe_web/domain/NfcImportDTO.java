package com.satoriviacafe.cafe_web.domain;

import com.satoriviacafe.common.annotation.Excel;
import lombok.Data;


/**
 * NFC导入DTO
 *
 * @author shy
 * @since 2026年06月28日
 */
@Data
public class NfcImportDTO {
    /**
     * NFC编码
     */
    @Excel(name = "NFC编码")
    private String nfcCode;
    /**
     * SKU编码
     */
    @Excel(name = "SKU编码")
    private String skuCode;
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
}

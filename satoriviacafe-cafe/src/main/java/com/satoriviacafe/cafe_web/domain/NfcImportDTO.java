package com.satoriviacafe.cafe_web.domain;

import lombok.Data;

/**
 * NFC导入DTO
 *
 * @author shy
 * @since 2026年06月28日
 */
@Data
public class NfcImportDTO {
    private String nfcCode;
    private String skuCode;
    private String productName;
    private String batchName;
}

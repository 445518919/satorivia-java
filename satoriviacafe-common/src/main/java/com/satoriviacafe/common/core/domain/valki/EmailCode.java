package com.satoriviacafe.common.core.domain.satoriviacafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shy
 * @since 2025年05月10日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailCode {

    private String email;
    private VerificationCode verificationCode;

    public boolean checkNull() {
        return email == null || verificationCode == null || verificationCode.getCode() == null || verificationCode.getUuid() == null;
    }
}

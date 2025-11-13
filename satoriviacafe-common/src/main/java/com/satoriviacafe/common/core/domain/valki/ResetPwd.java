package com.satoriviacafe.common.core.domain.satoriviacafe;

import lombok.Data;

/**
 * @author shy
 * @since 2025年05月12日
 */
@Data
public class ResetPwd {
    String oldPassword;
    String newPassword;
}

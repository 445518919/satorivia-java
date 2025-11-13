package com.satoriviacafe.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RetCode {
    NONE(0),
    ACCOUNT_NOT_EXIST(1),
    ACCOUNT_CREATE_SUCC(2),
    ACCOUNT_ALREADY_EXIST(3),
    INVALID_USERNAME_OR_EMAIL(4),
    INVAILD_PASSWORD(5),
    INVALID_JSON_CONTENT(6),
    INCORRECT_PASSWORD(7),
    LOGIN_SUCC(8),
    LOGIN_FAIL(9),
    PASSWORD_RESET_SENT(10),
    PASSWORD_RESET_SUCC(11),
    INVALID_VERIFY_CODE(12),
    ACCOUNT_EMAIL_VERIFIED(13);

    final Integer code;

    public static RetCode getByCode(Integer code) {
        for (RetCode value : RetCode.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NONE;
    }
}

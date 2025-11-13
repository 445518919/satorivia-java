package com.satoriviacafe.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RarityType {
    精選(1),
    奢華(2),
    尊爵(3),
    究極(4),
    限定(5);

    final Integer code;

    public static RarityType getByCode(Integer code) {
        for (RarityType rarityType : RarityType.values()) {
            if (rarityType.getCode().equals(code)) {
                return rarityType;
            }
        }
        return null;
    }
}

package com.satoriviacafe.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author shy
 * @since 2025年04月29日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MultiLanguage {

    /**
     * 多语言对象的唯一标识符
     */
    String uuid;

    /**
     * 多语言对象的显示名称
     */
    String displayName;

    /**
     * 多语言对象的语言代码
     */
    String langCode;

    /**
     * 创建并添加 MultiLanguage 对象到待保存列表
     *
     * @param listToSave  待保存的 MultiLanguage 对象列表
     * @param uuid        对象的 UUID
     * @param displayName 对象的显示名称
     * @param langCode    语言代码
     */
    static public void addMultiLanguageEntry(List<MultiLanguage> listToSave, String uuid, String displayName, String langCode) {
        // 避免添加 displayName 为 null 或空的条目，或者 uuid 为 null 的条目
        if (uuid == null || displayName == null || displayName.trim().isEmpty()) {
            return;
        }
        listToSave.add(new MultiLanguage(uuid, displayName, langCode));
    }
}

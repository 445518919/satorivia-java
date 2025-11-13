package com.satoriviacafe.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 集合工具类
 *
 * @author shy
 * @since 2025年10月07日
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return 结果
     */
    public static boolean isEmpty(java.util.Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否非空
     *
     * @param collection 集合
     * @return 结果
     */
    public static boolean isNotEmpty(java.util.Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean contains(java.util.Collection<?> collection, Object element) {
        return collection != null && collection.contains(element);
    }
}

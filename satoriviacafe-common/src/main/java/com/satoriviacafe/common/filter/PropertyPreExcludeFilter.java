package com.satoriviacafe.common.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 排除JSON敏感属性（Jackson实现）
 *
 * @author satoriviacafe
 */
public class PropertyPreExcludeFilter extends SimpleBeanPropertyFilter {

    // 存储需要排除的属性名
    private final Set<String> excludes = new HashSet<>();

    public PropertyPreExcludeFilter() {
    }

    /**
     * 添加需要排除的属性
     *
     * @param filters 要排除的属性名数组
     * @return 当前过滤器实例（链式调用）
     */
    public PropertyPreExcludeFilter addExcludes(String... filters) {
        if (filters != null && filters.length > 0) {
            Collections.addAll(excludes, filters);
        }
        return this;
    }

    /**
     * 重写属性序列化逻辑：排除指定属性
     */
    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
            throws Exception {
        // 如果属性名在排除列表中，则不序列化该字段
        if (excludes.contains(writer.getName())) {
            return;
        }
        // 否则正常序列化
        super.serializeAsField(pojo, jgen, provider, writer);
    }
}

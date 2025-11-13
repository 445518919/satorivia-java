package com.satoriviacafe.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.satoriviacafe.common.filter.PropertyPreExcludeFilter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Jackson JSON 工具类（统一封装 ObjectMapper，避免重复配置）
 * 新增：支持带 PropertyPreExcludeFilter 的序列化方法，适配日志敏感字段排除
 *
 * @author satoriviacafe
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    // -------------------------- 通用日期格式配置 --------------------------
    public static final String STANDARD_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";
    public static final String STANDARD_TIME_FORMAT = "HH:mm:ss";
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    static {
        OBJECT_MAPPER = new ObjectMapper();

        // 线程安全的日期格式 + 时区配置
        StdDateFormat stdDateFormat = new StdDateFormat();
        stdDateFormat.setTimeZone(DEFAULT_TIME_ZONE);
        OBJECT_MAPPER.setDateFormat(stdDateFormat);
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIME_ZONE);

        // Java 8 时间类型支持
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(STANDARD_DATE_TIME_FORMAT);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dtf));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dtf));
        OBJECT_MAPPER.registerModule(javaTimeModule);

        // 基础配置
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    // -------------------------- 原有核心方法（不变） --------------------------
    public static String toJsonStringNullSafe(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("toJsonStringNullSafe: obj 不能为 null");
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 序列化失败：" + obj.getClass().getSimpleName(), e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 序列化失败：" + (obj == null ? "null" : obj.getClass().getSimpleName()), e);
        }
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (VStringUtils.isEmpty(jsonStr)) {
            throw new IllegalArgumentException("JSON 字符串不能为空");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 解析失败：目标类型=" + clazz.getSimpleName(), e);
        }
    }

    public static <T> T parseObject(String jsonStr, TypeReference<T> typeReference) {
        if (VStringUtils.isEmpty(jsonStr)) {
            throw new IllegalArgumentException("JSON 字符串不能为空");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 泛型解析失败", e);
        }
    }

    public static Map<String, Object> parseMap(String jsonStr) {
        return parseObject(jsonStr, new TypeReference<>() {});
    }

    public static <T> Map<String, T> parseMap(String jsonStr, Class<T> valueType) {
        if (VStringUtils.isEmpty(jsonStr)) {
            throw new IllegalArgumentException("JSON 字符串不能为空");
        }
        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, String.class, valueType);
            return OBJECT_MAPPER.readValue(jsonStr, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 解析为 Map<String," + valueType.getSimpleName() + "> 失败", e);
        }
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> elementType) {
        if (VStringUtils.isEmpty(jsonStr)) {
            throw new IllegalArgumentException("JSON 字符串不能为空");
        }
        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
            return OBJECT_MAPPER.readValue(jsonStr, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 解析为 List<" + elementType.getSimpleName() + "> 失败", e);
        }
    }

    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("objectToMap: obj 不能为 null");
        }
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<>() {
        });
    }

    // -------------------------- 新增：带 PropertyPreExcludeFilter 的序列化方法 --------------------------

    /**
     * 带敏感字段排除的 JSON 序列化（适配日志打印场景）
     *
     * @param obj    要序列化的对象（可为 null，null 时返回 "null"）
     * @param filter 敏感字段过滤器（如排除 password）
     * @return 过滤后的 JSON 字符串
     * @throws RuntimeException 序列化失败时抛出
     */
    public static String toJsonStringWithFilter(Object obj, String id, PropertyPreExcludeFilter filter) {
        if (filter == null) {
            // 无过滤器时，直接调用默认序列化
            return toJsonString(obj);
        }
        try {
            // 注册过滤器并序列化
            SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter(id, filter) // 过滤器 ID 与 LogAspect 保持一致
                    .setFailOnUnknownId(false); // 忽略未定义的过滤器 ID（避免异常）
            return OBJECT_MAPPER.writer(filterProvider).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 带过滤器序列化失败：" + (obj == null ? "null" : obj.getClass().getSimpleName()), e);
        }
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}

package com.satoriviacafe.framework.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import com.satoriviacafe.common.utils.JSONUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis使用Jackson序列化
 * 替代FastJSON实现，保持类型安全和兼容性
 *
 * @author satoriviacafe
 */
@SuppressWarnings("unused")
public class Jackson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final ObjectMapper objectMapper;
    private final Class<T> clazz;

    public Jackson2JsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
        // 使用全局统一的ObjectMapper配置，保证序列化规则一致
        this.objectMapper = JSONUtil.getObjectMapper().copy();

        // 关键配置：启用类型信息存储（替代FastJSON的WriteClassName）
        // 仅允许白名单内的类进行类型转换，增强安全性
        this.objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY
        );
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) return new byte[0];
        try {
            // 序列化时包含类型信息
            return objectMapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Jackson序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) return null;
        try {
            // 反序列化时根据类型信息还原对象
            return objectMapper.readValue(bytes, clazz);
        } catch (Exception e) {
            throw new SerializationException("Jackson反序列化失败: " + e.getMessage(), e);
        }
    }
}

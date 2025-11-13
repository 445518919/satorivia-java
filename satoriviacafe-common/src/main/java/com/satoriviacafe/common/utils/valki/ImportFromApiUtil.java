package com.satoriviacafe.common.utils.satoriviacafe;

import com.satoriviacafe.common.utils.JSONUtil; // 导入项目自定义JSONUtil
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API数据导入工具类（基于项目自定义JSONUtil实现）
 *
 * @author shy
 * @since 2025年05月17日
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ImportFromApiUtil {

    private final CloseableHttpClient httpClient;

    /**
     * 异步POST请求获取列表
     */
    @Async
    public <T> CompletableFuture<List<T>> postListFromApiAsync(
            String apiUrl, Class<T> tClass, Map<String, Object> requestBody, Map<String, String> headers, String dataName) {
        return CompletableFuture.supplyAsync(() -> postListFromApi(apiUrl, tClass, requestBody, headers, dataName));
    }

    /**
     * 异步GET请求获取列表
     */
    @Async
    public <T> CompletableFuture<List<T>> getListFromApiAsync(
            String apiUrl, Class<T> tClass, Map<String, String> headers, String dataName) {
        return CompletableFuture.supplyAsync(() -> getListFromApi(apiUrl, tClass, headers, dataName));
    }

    /**
     * POST请求获取列表
     */
    public <T> List<T> postListFromApi(
            String apiUrl, Class<T> tClass, Map<String, Object> requestBody, Map<String, String> headers, String dataName) {
        log.info("POST {} payload: {}", apiUrl, JSONUtil.toJsonString(requestBody)); // 替换Hutool的toJsonStr
        try {
            String responseBody = sendPost(apiUrl, requestBody, headers);
            return parseListFromBody(responseBody, apiUrl, tClass, dataName);
        } catch (Exception e) {
            log.error("POST {} failed: {}", apiUrl, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * POST请求获取列表（无自定义头）
     */
    public <T> List<T> postListFromApi(
            String apiUrl, Class<T> tClass, Map<String, Object> requestBody, String dataName) {
        return postListFromApi(apiUrl, tClass, requestBody, null, dataName);
    }

    /**
     * GET请求获取列表
     */
    public <T> List<T> getListFromApi(
            String apiUrl, Class<T> tClass, Map<String, String> headers, String dataName) {
        log.info("GET {}", apiUrl);
        try {
            String responseBody = sendGet(apiUrl, headers);
            return parseListFromBody(responseBody, apiUrl, tClass, dataName);
        } catch (Exception e) {
            log.error("GET {} failed: {}", apiUrl, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * GET请求获取列表（无自定义头）
     */
    public <T> List<T> getListFromApi(String apiUrl, Class<T> tClass, String dataName) {
        return getListFromApi(apiUrl, tClass, null, dataName);
    }

    /**
     * 发送POST请求
     */
    private String sendPost(String apiUrl, Map<String, Object> requestBody, Map<String, String> headers) {
        HttpPost post = new HttpPost(apiUrl);
        // 替换Hutool的toJsonStr为项目JSONUtil的toJsonString，兼容null值
        String jsonBody = JSONUtil.toJsonString(requestBody != null ? requestBody : Collections.emptyMap());
        post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
        return sendRequest(post, headers);
    }

    /**
     * 发送GET请求
     */
    private String sendGet(String apiUrl, Map<String, String> headers) {
        return sendRequest(new HttpGet(apiUrl), headers);
    }

    /**
     * 发送HTTP请求通用方法
     */
    private String sendRequest(HttpUriRequestBase req, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpClientResponseHandler<String> responseHandler = response -> {
            int status = response.getCode();
            if (status == HttpStatus.SC_OK) {
                return response.getEntity() != null ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8) : null;
            } else {
                log.error("Unexpected response status from {}: {}, ", req, status);
                return "";
            }
        };
        try {
            return httpClient.execute(req, responseHandler);
        } catch (IOException e) {
            log.error("Request to {} failed: {}", req, e.getMessage(), e);
            return "";
        }
    }

    /**
     * 解析HTTP响应字符串中的列表数据
     */
    public <T> List<T> parseListFromBody(String responseBody, String apiUrl, Class<T> tClass, String dataName) {
        if (responseBody == null || responseBody.isEmpty()) {
            log.error("Empty response from {}", apiUrl);
            return Collections.emptyList();
        }
        try {
            // 替换Hutool的JSONUtil.parseObj为项目JSONUtil的parseObject
            Map<String, Object> json = JSONUtil.parseMap(responseBody);
            Object dataObj = json.get(dataName);

            // 转换数据为JSON字符串后解析为列表（兼容不同数据类型）
            if (dataObj == null) {
                log.error("Field '{}' is null in response from {}", dataName, apiUrl);
                return Collections.emptyList();
            }

            // 将数据对象转换为JSON字符串，再解析为目标类型列表
            String dataJson = JSONUtil.toJsonString(dataObj);
            return JSONUtil.parseList(dataJson, tClass); // 替换Hutool的toList
        } catch (Exception e) {
            log.error("Parse list from {} failed: {}", apiUrl, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}

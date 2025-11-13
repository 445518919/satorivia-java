package com.satoriviacafe.common.utils.ip;

import lombok.extern.slf4j.Slf4j;
import com.satoriviacafe.common.config.SatoriviacafeConfig;
import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.http.HttpUtils;
import com.satoriviacafe.common.utils.JSONUtil;  // 引入JsonUtils（假设工具类名为JSONUtil）

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取地址类（使用JsonUtils解析JSON）
 *
 * @author satoriviacafe
 */
@Slf4j
public class AddressUtils {

    // 固定 host，防止SSRF风险
    private static final String SCHEME = "http";
    private static final String HOST = "whois.pconline.com.cn";
    private static final String PATH = "/ipJson.jsp";
    public static final String UNKNOWN = "XX XX";

    // IPv4格式校验正则
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\."
                    + "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\."
                    + "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\."
                    + "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$"
    );

    /**
     * 通过IP获取真实地址（使用JsonUtils解析响应）
     */
    public static String getRealAddressByIP(String ip) {
        // 1. 非空校验
        if (ip == null) {
            log.warn("IP 为空，不查地理位置");
            return UNKNOWN;
        }
        // 2. IP格式校验
        if (!IPV4_PATTERN.matcher(ip).matches()) {
            log.warn("非法 IP 格式，不查地理位置: {}", ip);
            return UNKNOWN;
        }
        // 3. 内网/本地地址判断
        try {
            InetAddress addr = InetAddress.getByName(ip);
            if (addr.isAnyLocalAddress()
                    || addr.isLoopbackAddress()
                    || addr.isSiteLocalAddress()
                    || addr.isLinkLocalAddress()
                    || addr.isMulticastAddress()) {
                log.warn("内网/本地地址不查询: {}", ip);
                return "内网IP";
            }
        } catch (UnknownHostException e) {
            log.warn("IP 解析失败，不查地理位置: {}", ip);
            return UNKNOWN;
        }
        // 4. 全局开关判断
        if (!SatoriviacafeConfig.isAddressEnabled()) {
            return UNKNOWN;
        }
        // 5. 构造请求并解析响应（核心改造：使用JsonUtils替换fastJson）
        try {
            // 构造查询参数
            String query = "ip="
                    + URLEncoder.encode(ip, StandardCharsets.UTF_8)
                    + "&json=true";
            URI uri = new URI(SCHEME, HOST, PATH, query, null);

            // 发送请求
            String rsp = HttpUtils.sendGet(uri.toASCIIString(), Constants.GBK);
            if (VStringUtils.isEmpty(rsp)) {
                log.error("获取地理位置异常: 返回空, {}", ip);
                return UNKNOWN;
            }

            // 核心替换：使用JsonUtils解析JSON为Map（替代fastJson的JSONObject）
            Map<String, Object> obj = JSONUtil.parseMap(rsp);

            // 提取省份和城市（兼容null值处理）
            String pro = obj.getOrDefault("pro", "").toString();
            String city = obj.getOrDefault("city", "").toString();

            // 处理空值场景
            if (VStringUtils.isEmpty(pro) && VStringUtils.isEmpty(city)) {
                return UNKNOWN;
            }
            return pro + " " + city;
        } catch (Exception e) {
            log.error("获取地理位置异常: {}，{}", ip, e.getMessage(), e);
        }
        return UNKNOWN;
    }
}

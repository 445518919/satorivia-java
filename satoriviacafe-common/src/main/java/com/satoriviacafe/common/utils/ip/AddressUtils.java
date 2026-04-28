package com.satoriviacafe.common.utils.ip;

import com.satoriviacafe.common.utils.VStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.service.Config;
import org.lionsoul.ip2region.service.ConfigBuilder;
import org.lionsoul.ip2region.service.Ip2Region;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 获取地址类（离线 IP 库版）
 *
 * @author valki
 */


@Slf4j
@SuppressWarnings("preview")
public class AddressUtils {

    public static final String UNKNOWN = "XX XX";
    private static final StableValue<Ip2Region> ip2Region = StableValue.of();

    public static Ip2Region getIp2Region() {
        return ip2Region.orElseSet(AddressUtils::initIp2Region);
    }

    /**
     * 初始化 IP 库
     */
    private static Ip2Region initIp2Region() {
        String v4Path = "data/ip2region_v4.xdb";
        String v6Path = "data/ip2region_v6.xdb";

        Config v4Config = buildConfigFromResource(v4Path, true);
        Config v6Config = buildConfigFromResource(v6Path, false);

        // 这里保留你的严格检查逻辑：如果两个库不全，则不启用
        if (v4Config == null || v6Config == null) {
            log.error("IP库加载不完整 -> v4: {}, v6: {}", v4Config != null, v6Config != null);
            return null;
        }
        try {
            log.info("Ip2Region (v4+v6) 离线库加载成功");
            return Ip2Region.create(v4Config, v6Config);
        } catch (IOException e) {
            log.error("初始化 Ip2Region 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 核心适配方法：从 classpath 读取流并配置
     */
    private static Config buildConfigFromResource(String path, boolean isV4) {
        ClassPathResource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            log.warn("IP库文件不存在: classpath:{}", path);
            return null;
        }

        try (InputStream is = resource.getInputStream()) {
            ConfigBuilder builder = Config.custom()
                    .setXdbInputStream(is)
                    .setCachePolicy(Config.BufferCache) // 必须全量缓存
                    .setSearchers(15);
            return isV4 ? builder.asV4() : builder.asV6();

        } catch (Exception e) {
            log.error("读取 IP 库文件失败 [{}]: {}", path, e.getMessage());
            return null;
        }
    }

    /**
     * 通过IP获取真实地址
     */
    public static String getRealAddressByIP(String ip) {
        if (VStringUtils.isBlank(ip)) return UNKNOWN;
        if (IpUtils.isInternalIpQuietly(ip)) return "内网IP";
        try {
            // 如果初始化失败，直接返回未知，不报错
            Ip2Region ip2Region = getIp2Region();
            if (ip2Region == null) return UNKNOWN;
            String region = ip2Region.search(ip);
            // 校验返回结果
            if (VStringUtils.isBlank(region) || "0".equals(region)) return UNKNOWN;
            return region;

        } catch (Exception e) {
            // 降低日志级别，防止恶意扫描导致日志爆炸
            log.warn("解析IP异常 {} : {}", ip, e.getMessage());
        }
        return UNKNOWN;
    }
}


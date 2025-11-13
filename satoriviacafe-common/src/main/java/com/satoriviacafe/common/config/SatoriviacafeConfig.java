package com.satoriviacafe.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author satoriviacafe
 */
@Data
@Component
@ConfigurationProperties(prefix = "satoriviacafe")
public class SatoriviacafeConfig {

    private boolean virtual = true;

    /**
     * 是否为开发环境
     */
    private boolean dev = false;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 默认角色ID
     */
    private Long defaultRoleId = 2L;

    /**
     * 上传路径
     */
    @Getter
    private static String profile;

    /**
     * 获取地址开关
     */
    @Getter
    private static boolean addressEnabled;

    /**
     * 验证码类型
     */
    @Getter
    private static String captchaType;


    public void setProfile(String profile) {
        SatoriviacafeConfig.profile = profile;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        SatoriviacafeConfig.addressEnabled = addressEnabled;
    }

    public void setCaptchaType(String captchaType) {
        SatoriviacafeConfig.captchaType = captchaType;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return "%s/import".formatted(getProfile());
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return "%s/avatar".formatted(getProfile());
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return "%s/download/".formatted(getProfile());
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return "%s/upload".formatted(getProfile());
    }
}

package com.satoriviacafe.chain.config;

import com.alipay.mychain.taas.api.common.VersionCode;
import com.alipay.mychain.taas.api.enums.SystemCodeEnum;
import com.alipay.mychain.taas.common.client.TaasClient;
import com.alipay.mychain.taas.common.factory.ServiceFactory;
import com.alipay.mychain.taas.sdk.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.satoriviacafe.chain.constants.Constants.*;

/**
 * @author demo
 * @since 2025年09月02日
 * Taas相关服务配置类，将所有服务注册为Spring Bean
 **/
@SuppressWarnings("deprecation")
@Configuration
public class TassConfig {

    /**
     * 创建taasClient
     *
     * @return TaasClient
     */
    @Bean
    public TaasClient taasClient() {
        return TaasClient.newBuilder()
                .appId(APP_ID)
                .host(HOST)
                .priKey(APP_PRIVATE_KEY)
                .versionCode(VersionCode.V_COMMON)
                .systemCode(SystemCodeEnum.UNIVERSAL_SYSTEM.name())
                // 可根据需要配置超时参数
                //.options(ClientOptions.newBuilder().readTimeOut(10L).connectTimeOut(10L).build())
                .build();
    }


    /**
     * 溯源批次管理服务
     */
    @Bean
    public TraceBatchService traceBatchService(TaasClient taasClient) {
        return ServiceFactory.register(TraceBatchService.class, taasClient);
    }

    /**
     * 溯源物料清单服务（文档“物料清单”模块）
     */
    @Bean
    public TraceBomService traceBomService(TaasClient taasClient) {
        return ServiceFactory.register(TraceBomService.class, taasClient);
    }

    /**
     * 溯源证书服务（文档“证书”模块）
     */
    @Bean
    public TraceCertficateService traceCertficateService(TaasClient taasClient) {
        return ServiceFactory.register(TraceCertficateService.class, taasClient);
    }

    /**
     * 溯源渠道模板服务（文档“渠道模板”模块）
     */
    @Bean
    public TraceChannelTemplateService traceChannelTemplateService(TaasClient taasClient) {
        return ServiceFactory.register(TraceChannelTemplateService.class, taasClient);
    }


    /**
     * 溯源码服务类接口
     */
    @Bean
    public TraceCodeService traceCodeService(TaasClient taasClient) {
        return ServiceFactory.register(TraceCodeService.class, taasClient);
    }

    /**
     * 溯源核心服务
     */
    @Bean
    public TraceCoreService traceCoreService(TaasClient taasClient) {
        return ServiceFactory.register(TraceCoreService.class, taasClient);
    }

    /**
     * 溯源存证服务（文档“存证”模块）
     */
    @Bean
    public TraceDepositService traceDepositService(TaasClient taasClient) {
        return ServiceFactory.register(TraceDepositService.class, taasClient);
    }

    /**
     * 溯源文件上传服务（文档“文件上传”模块）
     */
    @Bean
    public TraceFileUploadService traceFileUploadService(TaasClient taasClient) {
        return ServiceFactory.register(TraceFileUploadService.class, taasClient);
    }

    /**
     * 溯源码等级码服务（文档“等级码”模块）
     */
    @Bean
    public TraceLevelCodeService traceLevelCodeService(TaasClient taasClient) {
        return ServiceFactory.register(TraceLevelCodeService.class, taasClient);
    }

    /**
     * 溯源物流管理服务
     */
    @Bean
    public TraceLogisticsService traceLogisticsService(TaasClient taasClient) {
        return ServiceFactory.register(TraceLogisticsService.class, taasClient);
    }

    /**
     * 溯源物料管理服务（文档“物料清单”模块）
     */
    @Bean
    public TraceMaterialService traceMaterialService(TaasClient taasClient) {
        return ServiceFactory.register(TraceMaterialService.class, taasClient);
    }

    /**
     * 溯源品牌商管理服务
     */
    @Bean
    public TraceMerchantService traceMerchantService(TaasClient taasClient) {
        return ServiceFactory.register(TraceMerchantService.class, taasClient);
    }

    /**
     * 溯源小程序/h5显示样式模板配置服务
     */
    @Bean
    public TraceMiniTemplateService traceMiniTemplateService(TaasClient taasClient) {
        return ServiceFactory.register(TraceMiniTemplateService.class, taasClient);
    }

    /**
     * 溯源环节管理服务
     */
    @Bean
    public TracePhaseService tracePhaseService(TaasClient taasClient) {
        return ServiceFactory.register(TracePhaseService.class, taasClient);
    }

    /**
     * 溯源商品管理服务
     */
    @Bean
    public TraceProductService traceProductService(TaasClient taasClient) {
        return ServiceFactory.register(TraceProductService.class, taasClient);
    }

    /**
     * 溯源隔离上报服务（文档“隔离上报”模块）
     */
    @Bean
    public TraceQuarantineReportService traceQuarantineReportService(TaasClient taasClient) {
        return ServiceFactory.register(TraceQuarantineReportService.class, taasClient);
    }

    /**
     * 高级溯源数据查询服务（文档“数据查询”模块）
     */
    @Bean
    public TraceQueryService traceQueryService(TaasClient taasClient) {
        return ServiceFactory.register(TraceQueryService.class, taasClient);
    }

    /**
     * 溯源登记服务api
     */
    @Bean
    public TraceRegisterService traceRegisterService(TaasClient taasClient) {
        return ServiceFactory.register(TraceRegisterService.class, taasClient);
    }

    /**
     * 溯源码规则管理服务
     */
    @Bean
    public TraceRuleService traceRuleService(TaasClient taasClient) {
        return ServiceFactory.register(TraceRuleService.class, taasClient);
    }

    /**
     * 溯源扫码服务
     */
    @Bean
    public TraceScanCodeService traceScanCodeService(TaasClient taasClient) {
        return ServiceFactory.register(TraceScanCodeService.class, taasClient);
    }

    /**
     * 溯源扫码预警规则服务
     */
    @Bean
    public TraceScanCodeWarnRuleService traceScanCodeWarnRuleService(TaasClient taasClient) {
        return ServiceFactory.register(TraceScanCodeWarnRuleService.class, taasClient);
    }

    /**
     * 溯源扫码预警服务
     */
    @Bean
    public TraceScanCodeWarnService traceScanCodeWarnService(TaasClient taasClient) {
        return ServiceFactory.register(TraceScanCodeWarnService.class, taasClient);
    }

    /**
     * 店铺导购服务
     */
    @Bean
    public TraceShopGuideService traceShopGuideService(TaasClient taasClient) {
        return ServiceFactory.register(TraceShopGuideService.class, taasClient);
    }

    /**
     * 店铺管理服务
     */
    @Bean
    public TraceShopManageService traceShopManageService(TaasClient taasClient) {
        return ServiceFactory.register(TraceShopManageService.class, taasClient);
    }

    /**
     * 溯源数据统计服务
     */
    @Bean
    public TraceStatService traceStatService(TaasClient taasClient) {
        return ServiceFactory.register(TraceStatService.class, taasClient);
    }

    /**
     * 溯源上传服务（文档“数据上传”模块）
     */
    @Bean
    public TraceUploadService traceUploadService(TaasClient taasClient) {
        return ServiceFactory.register(TraceUploadService.class, taasClient);
    }


    /**
     * 溯源消费者服务接口
     */
    @Bean
    public TraceUserService traceUserService(TaasClient taasClient) {
        return ServiceFactory.register(TraceUserService.class, taasClient);
    }

    /**
     * 溯源批次管理服务（新）
     */
    @Bean
    public TsPhaseService tsPhaseService(TaasClient taasClient) {
        return ServiceFactory.register(TsPhaseService.class, taasClient);
    }

    /**
     * 溯源批次管理服务（新）
     */
    @Bean
    public TsBatchService tsBatchService(TaasClient taasClient) {
        return ServiceFactory.register(TsBatchService.class, taasClient);
    }

}

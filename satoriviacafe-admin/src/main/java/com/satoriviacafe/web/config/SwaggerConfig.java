package com.satoriviacafe.web.config; // Or your preferred config package

import com.satoriviacafe.common.config.SatoriviacafeConfig;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * OpenAPI 3 (Swagger) Configuration using springdoc-openapi
 *
 * @author satoriviacafe
 */
@Configuration
// Only create this configuration if swagger.enabled is true (matches your old 'enable(enabled)' logic)
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class SwaggerConfig {

    private final SatoriviacafeConfig satoriviacafeConfig; // Corrected variable name to follow Java conventions

    @Value("${swagger.pathMapping:}") // Provide a default empty string if not set
    private String pathMapping;

    @Value("${swagger.enabled:false}") // Provide a default empty string if not set
    private String enabled;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Authorization";

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("标题：Satoriviacafe管理系统_接口文档")
                        .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
                        .version("版本号:" + (satoriviacafeConfig != null ? satoriviacafeConfig.getVersion() : "UNKNOWN"))
                        .contact(new Contact()
                                        .name(satoriviacafeConfig != null ? satoriviacafeConfig.getName() : "Satoriviacafe Team")
                                // .url("Your contact URL") // Optional
                                // .email("Your contact email") // Optional
                        )
                )
                // Define the security scheme (API Key in Header)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Enter JWT Bearer token in the format 'Bearer {token}' or just '{token}' depending on your setup")
                        )
                )
                // Add a global security requirement. All operations will require the "Authorization" header.
                // You can also apply security per-operation using @SecurityRequirement annotation on controller methods.
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));

        // Configure server URL if pathMapping is used as a base path for APIs
        if (StringUtils.hasText(pathMapping)) {
            // This assumes pathMapping is a relative or absolute path prefix for your APIs.
            // e.g., if pathMapping is "/api/v1", all documented endpoints will appear under this base.
            openAPI.servers(List.of(new Server().url(pathMapping).description("Mapped server URL")));
        }

        return openAPI;
    }
}

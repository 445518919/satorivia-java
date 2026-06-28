package com.satoriviacafe.framework.config;

import com.satoriviacafe.framework.config.properties.PermitAllUrlProperties;
import com.satoriviacafe.framework.security.filter.JwtAuthenticationTokenFilter;
import com.satoriviacafe.framework.security.handle.AuthenticationEntryPointImpl;
import com.satoriviacafe.framework.security.handle.LogoutSuccessHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * spring security配置
 *
 * @author satoriviacafe
 */
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    /**
     * 自定义用户认证逻辑
     */
    private final UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    private final AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    private final JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    private final CorsFilter corsFilter;

    /**
     * 允许匿名访问的地址
     */
    private final PermitAllUrlProperties permitAllUrl;

    /**
     * 身份验证实现
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // CSRF禁用，因为不使用session
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用HTTP响应标头，允许缓存控制和同源框架
                .headers((headersCustomizer) -> headersCustomizer
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable).frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 异步请求/虚拟线程也能拿到 SecurityContext
                .addFilterBefore(new WebAsyncManagerIntegrationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 认证失败处理类
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 注解标记允许匿名访问的url
                .authorizeHttpRequests((requests) -> {
                    // 处理 PermitAllUrlProperties 中配置的URL
                    if (permitAllUrl != null && permitAllUrl.getUrls() != null) {
                        permitAllUrl.getUrls().forEach(url -> requests.requestMatchers(url).permitAll());
                    }
                    // 对于登录login 注册register 验证码captchaImage 等特定路径允许匿名访问
                    requests.requestMatchers("/satoriviacafe/version", "/login", "/register", "/captchaImage", "/track/**") // 注意移除了重复的 /v2/auth/verify
                            .permitAll()
                            // 静态资源，可匿名访问
                            .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**.html", "/**.css", "/**.js", "/profile/**").permitAll()
                            // Swagger 和 Druid 相关路径，可匿名访问
                            .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/*/api-docs/**", "/v3/api-docs/**", "/druid/**").permitAll() // 更新了swagger路径以兼容springdoc-openapi
                            // 除上面外的所有请求全部需要鉴权认证
                            .anyRequest().authenticated();
                })
                // 添加Logout filter
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
                // 添加JWT filter 在 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加CORS filter 在 JwtAuthenticationTokenFilter 和 LogoutFilter 之前
                // 通常CORS filter应该非常靠前，在安全过滤器链之前或早期
                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class) // 可以保留，但下面的更通用
                // .addFilterBefore(corsFilter, LogoutFilter.class) // 如果上面的存在，这个可能多余，除非顺序有特殊要求
                // 更常见的做法是，如果CorsFilter是Spring框架的CorsFilter，它通常在更早的阶段或者通过 HttpSecurity.cors() 配置
                // 如果 corsFilter 是自定义的，确保它放置在需要它的过滤器之前，例如在认证过滤器之前
                .build();
    }
    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

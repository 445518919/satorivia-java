package com.satoriviacafe.framework.web.service;

import com.satoriviacafe.common.constant.CacheConstants;
import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.core.domain.satoriviacafe.LoginUser;
import com.satoriviacafe.common.core.redis.RedisCache;
import com.satoriviacafe.common.utils.ServletUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.ip.AddressUtils;
import com.satoriviacafe.common.utils.ip.IpUtils;
import com.satoriviacafe.common.utils.uuid.IdUtils;
import com.satoriviacafe.framework.config.properties.TokenConfig;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author satoriviacafe
 */
@SuppressWarnings("unused")
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

    private final RedisCache redisCache;
    private final TokenConfig tokenConfig;

    private SecretKey sharedKey;
    private JwtParser jwtParser;

    @PostConstruct // 如果是Spring Bean，可以使用PostConstruct初始化
    public void init() { // 或者在构造函数中初始化
        this.sharedKey = Keys.hmacShaKeyFor(tokenConfig.getSecret().getBytes(StandardCharsets.UTF_8)); // 推荐指定字符集
        this.jwtParser = Jwts.parser()
                .verifyWith(this.sharedKey)
                .build();
    }


    /**
     * 获取用户身份信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     * @param token 令牌
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        if (VStringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     *
     * @param loginUser 用户信息
     *
     */
    public void setLoginUser(LoginUser loginUser) {
        if (VStringUtils.isNotNull(loginUser) && VStringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (VStringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 登录信息
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        long currentTimeMillis = System.currentTimeMillis();
        loginUser.setLoginTime(currentTimeMillis);
        long ext = tokenConfig.getExpireTime() * MILLIS_MINUTE;
        loginUser.setExpireTime(currentTimeMillis + ext);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, tokenConfig.getExpireTime(), TimeUnit.MINUTES);
    }

    public void refreshToken(String token) {
        LoginUser loginUser = getLoginUser(token);
        if (VStringUtils.isNotNull(loginUser)) {
            refreshToken(loginUser);
        }
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder().claims(claims)
                .signWith(sharedKey, Jwts.SIG.HS512).compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return jwtParser.parseSignedClaims(token) // 解析已签名的 Claims (JWS)
                .getPayload(); // 获取实际的 Claims
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        if (VStringUtils.isEmpty(token)) { // 先检查token是否为空
            return null;
        }
        try {
            Claims claims = parseToken(token); // parseToken 内部会处理JWT的有效性
            if (claims != null) {
                Object usernameObj = claims.get(Constants.JWT_USERNAME);
                if (usernameObj != null) {
                    return usernameObj.toString();
                } else {
                    // Token有效，但特定的claim不存在
                    log.warn("Username claim ('{}') not found in the token.", Constants.JWT_USERNAME);
                    return null;
                }
            } else {
                // parseToken 返回了 null（如果 parseToken 内部有 try-catch 并返回 null 的话）
                // 或者如果 parseToken 直接抛异常，这里的代码块不会执行
                return null;
            }
        } catch (JwtException e) { // 捕获所有 JWT 相关的解析和验证异常
            log.warn("Failed to get username from token due to JWT processing error: {}", e.getMessage());
            return null; // Token 无效或处理失败
        } catch (Exception e) { // 捕获其他潜在的运行时异常，例如 NullPointerException
            log.error("An unexpected error occurred while getting username from token: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取请求token
     *
     * @param request 请求对象
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenConfig.getHeader());
        if (VStringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}

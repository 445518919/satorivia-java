package com.satoriviacafe.framework.aspectj;

import com.satoriviacafe.common.annotation.DataSource;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.framework.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 多数据源处理
 *
 * @author satoriviacafe
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.satoriviacafe.common.annotation.DataSource)"
            + "|| @within(com.satoriviacafe.common.annotation.DataSource)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);

        if (VStringUtils.isNotNull(dataSource)) {
            // 用ScopedValue作用域包裹
            final Object[] result = new Object[1];
            DynamicDataSourceContextHolder.runWithDataSourceType(dataSource.value().name(), () -> {
                try {
                    result[0] = point.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            });
            if (result[0] instanceof RuntimeException re && re.getCause() != null) {
                // 还原原始异常
                throw re.getCause();
            }
            return result[0];
        }
        // 没注解直接执行
        return point.proceed();
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}

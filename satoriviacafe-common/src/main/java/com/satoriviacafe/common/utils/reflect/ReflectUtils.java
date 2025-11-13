package com.satoriviacafe.common.utils.reflect;

import com.satoriviacafe.common.core.text.Convert;
import com.satoriviacafe.common.utils.DateUtils;
import com.satoriviacafe.common.utils.VStringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * 主要支持对属性的反射访问与对方法的反射调用（含重载与 varargs 支持），并提供常见类型转换支持。
 */
@SuppressWarnings({"unchecked","unused", "UnusedReturnValue", "LoggingSimilarMessage"})
public class ReflectUtils {
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    // 简单缓存：key = className#methodName:argCount:argType1,argType2,...
    private static final Map<String, Method> methodCache = new ConcurrentHashMap<>();
    private static final Map<String, Field> fieldCache = new ConcurrentHashMap<>();

    // 配置：是否在转换 String 时自动去掉末尾 ".0"
    @Getter
    @Setter
    private static volatile boolean trimTrailingDotZero = true;

    // 可配置的转换器表：目标类型(通常 boxed 类型) -> converter(Object -> Object)
    private static final Map<Class<?>, Function<Object, Object>> converters = new ConcurrentHashMap<>();

    static {
        // 初始化常用转换器（目标为 boxed 类或其他常见类型）
        converters.put(String.class, o -> {
            String s = Convert.toStr(o);
            if (trimTrailingDotZero && s != null && VStringUtils.endsWith(s, ".0")) {
                return StringUtils.substringBefore(s, ".0");
            }
            return s;
        });
        converters.put(Integer.class, Convert::toInt);
        converters.put(Long.class, Convert::toLong);
        converters.put(Double.class, Convert::toDouble);
        converters.put(Float.class, Convert::toFloat);
        converters.put(Boolean.class, Convert::toBool);

        // 扩展支持：BigDecimal、Character、byte[]
        converters.put(BigDecimal.class, o -> switch (o) {
             case null -> null;
             case BigDecimal bigDecimal -> o;
             case Number number -> BigDecimal.valueOf(number.doubleValue());
             default -> {
                 String s = Convert.toStr(o);
                 try {
                     yield new BigDecimal(s);
                 } catch (Exception ex) {
                     logger.debug("BigDecimal converter failed for '{}': {}", o, ex.getMessage());
                     yield null;
                 }
             }
         });

        converters.put(Character.class, o -> {
            if (o == null) return null;
            if (o instanceof Character) return o;
            String s = Convert.toStr(o);
            if (s == null || s.isEmpty()) return null;
            return s.charAt(0);
        });

        // byte[] 的 converter：支持 String -> bytes、Number -> single-byte 表示、byte[] 直接返回
        converters.put(byte[].class, o -> switch (o) {
             case null -> null;
             case byte[] bytes -> o;
             case Number number -> {
                 int v = number.intValue();
                 yield new byte[]{(byte) v};
             }
             default -> {
                 String s = Convert.toStr(o);
                 if (s == null) yield null;
                 yield s.getBytes();
             }
         });
    }

    /**
     * 注册/覆盖目标类型的转换器。返回之前注册的转换器（若有），便于测试或恢复。
     */
    public static <T> Function<Object, T> registerConverter(Class<T> target, Function<Object, T> converter) {
        @SuppressWarnings("unchecked")
        Function<Object, T> prev = (Function<Object, T>) converters.put(target, (Function<Object, Object>) converter);
        return prev;
    }

    /**
     * 注销某个目标类型的转换器。
     */
    public static void unregisterConverter(Class<?> target) {
        converters.remove(target);
    }

    /**
     * 取得当前 converters 的不可变视图大小（用于测试/监控）。
     */
    public static int getConverterCount() {
        return converters.size();
    }

    /**
     * 清理 methodCache。
     */
    public static void clearMethodCache() {
        methodCache.clear();
    }

    /**
     * 清理 fieldCache。
     */
    public static void clearFieldCache() {
        fieldCache.clear();
    }

    /**
     * 获取 caches 大小（用于测试/监控）。
     */
    public static int getMethodCacheSize() {
        return methodCache.size();
    }

    public static int getFieldCacheSize() {
        return fieldCache.size();
    }

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, ".")) {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
        }
        return (E) object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value) {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
            } else {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[]{value});
            }
        }
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    @SuppressWarnings("unchecked")
    public static <E> E getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            logger.debug("在 [{}] 中，没有找到 [{}] 字段 ", obj == null ? "null" : obj.getClass(), fieldName);
            return null;
        }
        try {
            return (E) field.get(obj);
        } catch (IllegalAccessException e) {
            throw convertReflectionExceptionToUnchecked("无法读取字段值: " + fieldName, e);
        }
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static <E> void setFieldValue(final Object obj, final String fieldName, final E value) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            logger.debug("在 [{}] 中，没有找到 [{}] 字段 ", obj == null ? "null" : obj.getClass(), fieldName);
            return;
        }
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw convertReflectionExceptionToUnchecked("无法设置字段值: " + fieldName, e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 同时匹配方法名+参数类型。
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                     final Object[] args) {
        if (obj == null || methodName == null) {
            return null;
        }
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            logger.debug("[ReflectUtil] 在 [{}] 中，没有找到 [{}] 方法 ", obj.getClass(), methodName);
            return null;
        }
        try {
            return (E) method.invoke(obj, args);
        } catch (Exception e) {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + Arrays.toString(args);
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 只匹配函数名，如果有多个同名函数会尽量选择最合适的（根据参数可赋值性和可转换性评分）。
     * 支持 varargs 调用与常见类型转换，不会修改调用者传入的 args 数组（内部会 clone）。
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        if (obj == null || methodName == null) {
            return null;
        }
        int argCount = args == null ? 0 : args.length;
        Method method = getAccessibleMethodByName(obj, methodName, argCount, args);
        if (method == null) {
            logger.debug("在 [{}] 中，没有找到 [{}] 方法 ", obj.getClass(), methodName);
            return null;
        }
        try {
            Class<?>[] paramTypes = method.getParameterTypes();
            // clone 一份參數，避免修改呼叫者傳入的陣列
            Object[] callArgs = args == null ? new Object[0] : args.clone();

            // 处理 varargs 的各种边缘情况
            if (method.isVarArgs()) {
                int declared = paramTypes.length;
                Class<?> varArgArrayType = paramTypes[declared - 1];
                Class<?> varArgComponentType = varArgArrayType.getComponentType();

                if (callArgs.length == declared) {
                    // 情况 A: 调用者传入恰好 declared 个参数，最后一个可能是一个数组（用户已打包）
                    Object last = callArgs[declared - 1];
                    if (last != null && last.getClass().isArray()) {
                        // 如果数组组件类型能赋值给 varArgComponentType，则认为用户已预打包，直接传递
                        Class<?> lastComp = last.getClass().getComponentType();
                        if (!isAssignable(lastComp, varArgComponentType)) {
                            // 最后一个是数组但与 vararg component 不兼容，尝试按元素转换并打包成目标数组
                            int len = Array.getLength(last);
                            Object varArgArray = Array.newInstance(varArgComponentType, len);
                            for (int k = 0; k < len; k++) {
                                Object rawElem = Array.get(last, k);
                                Array.set(varArgArray, k, convertIfNeeded(rawElem, varArgComponentType));
                            }
                            callArgs[declared - 1] = varArgArray;
                        }
                    } else {
                        // 最后一个不是数组：将后面的单个元素视为 vararg 的第一个元素，打包剩余（其实只有一个）
                        Object varArgArray = Array.newInstance(varArgComponentType, 1);
                        Array.set(varArgArray, 0, convertIfNeeded(callArgs[declared - 1], varArgComponentType));
                        callArgs[declared - 1] = varArgArray;
                    }
                } else if (callArgs.length > declared) {
                    // 情况 B: 传入比 declared 多的参数，把尾部打包成 varargs 数组
                    int varArgCount = callArgs.length - (declared - 1);
                    Object varArgArray = Array.newInstance(varArgComponentType, varArgCount);
                    for (int k = 0; k < varArgCount; k++) {
                        Object raw = callArgs[declared - 1 + k];
                        Array.set(varArgArray, k, convertIfNeeded(raw, varArgComponentType));
                    }
                    Object[] newArgs = new Object[declared];
                    System.arraycopy(callArgs, 0, newArgs, 0, declared - 1);
                    newArgs[declared - 1] = varArgArray;
                    callArgs = newArgs;
                } else { // callArgs.length < declared
                    // 情况 C: 传入的参数少于 declared（即没有 varargs 元素），补一个空数组
                    Object varArgArray = Array.newInstance(varArgComponentType, 0);
                    Object[] newArgs = new Object[declared];
                    System.arraycopy(callArgs, 0, newArgs, 0, callArgs.length);
                    for (int i = callArgs.length; i < declared - 1; i++) {
                        newArgs[i] = null;
                    }
                    newArgs[declared - 1] = varArgArray;
                    callArgs = newArgs;
                }
                // paramTypes 保持为 method.getParameterTypes()
                paramTypes = method.getParameterTypes();
            }

            // 逐参数转换（非 varargs 的组件部分或 varargs 已按上面打包）
            for (int i = 0; i < paramTypes.length && i < callArgs.length; i++) {
                Object raw = callArgs[i];
                if (raw == null) continue;
                if (isAssignable(raw.getClass(), paramTypes[i])) {
                    continue;
                }
                callArgs[i] = convertIfNeeded(raw, paramTypes[i]);
            }

            return (E) method.invoke(obj, callArgs);
        } catch (Exception e) {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + Arrays.toString(args);
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * 将单个值转换为目标类型（若可能）。
     * 现在优先使用可配置的 converters Map；若无注册转换器，会使用内置策略（Date/Enum 等）。
     * 注意：此方法尽量稳健，失败时会返回原始值。
     */
    private static Object convertIfNeeded(Object raw, Class<?> target) {
        if (raw == null) return null;
        Class<?> boxed = boxIfPrimitive(target);

        // 优先使用注册的转换器
        Function<Object, Object> conv = converters.get(boxed);
        if (conv != null) {
            try {
                Object out = conv.apply(raw);
                if (out != null) return out;
                // 如果 converter 返回 null，继续尝试下面的兜底策略
            } catch (Exception ex) {
                logger.debug("converter for {} failed: {}", boxed, ex.getMessage());
                // fallback to built-in handling below
            }
        }

        // 处理数组目标（例如 byte[]），优先处理原始数组直接赋值或 String -> bytes
        if (target.isArray()) {
            Class<?> comp = target.getComponentType();
            if (comp == byte.class) {
                return switch (raw) {
                    case String s -> s.getBytes();
                    case Number number -> new byte[]{number.byteValue()};
                    default -> raw;
                };
            }
            // 其他数组类型暂不做额外处理；让调用方继续尝试基本的 assignable 判断
        }

        // 特殊处理 Date
        if (Date.class.equals(boxed)) {
            if (raw instanceof String s) {
                return DateUtils.parseDate(s);
            }
            if (raw instanceof Number n) {
                return DateUtil.getJavaDate(n.doubleValue());
            }
            return DateUtils.parseDate(raw.toString());
        }

        // BigDecimal 如果没被 converter 捕获也尝试一下
        if (BigDecimal.class.equals(boxed)) {
            if (raw instanceof Number) return BigDecimal.valueOf(((Number) raw).doubleValue());
            try {
                return new BigDecimal(Convert.toStr(raw));
            } catch (Exception ex) {
                logger.debug("BigDecimal fallback failed for {}: {}", raw, ex.getMessage());
            }
        }

        // 枚举处理（通用）
        if (target.isEnum() && raw instanceof String s) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            Class<? extends Enum> enumClass = (Class<? extends Enum>) target;
            try {
                return Enum.valueOf(enumClass, s);
            } catch (IllegalArgumentException ex) {
                for (Object constant : enumClass.getEnumConstants()) {
                    if (((Enum<?>) constant).name().equalsIgnoreCase(s)) {
                        return constant;
                    }
                }
                logger.debug("无法将字符串转为枚举 {}: {}", enumClass, s);
                throw new RuntimeException("无法将字符串映射到枚举: " + s);
            }
        }

        // 兜底：返回原值
        return raw;
    }

    private static Class<?> boxIfPrimitive(Class<?> cls) {
        if (cls == null) return null;
        return switch (cls.getName()) {
            case "int" -> Integer.class;
            case "long" -> Long.class;
            case "double" -> Double.class;
            case "float" -> Float.class;
            case "boolean" -> Boolean.class;
            case "char" -> Character.class;
            case "short" -> Short.class;
            case "byte" -> Byte.class;
            default -> cls;
        };
    }

    private static boolean isAssignable(Class<?> from, Class<?> to) {
        if (to.isPrimitive()) {
            to = boxIfPrimitive(to);
        }
        return to.isAssignableFrom(from);
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 使用缓存以减少反复查找。
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        if (obj == null) {
            return null;
        }
        Validate.notBlank(fieldName, "字段名不能为空");
        Class<?> cls = obj.getClass();
        String key = cls.getName() + "#" + fieldName;
        Field cached = fieldCache.get(key);
        if (cached != null) {
            return cached;
        }
        for (Class<?> superClass = cls; superClass != null && superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                fieldCache.putIfAbsent(key, field);
                return field;
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes) {
        if (obj == null) {
            return null;
        }
        Validate.notBlank(methodName, "方法名不能为空");
        Class<?> cls = obj.getClass();
        StringBuilder keyBuilder = new StringBuilder(cls.getName()).append("#").append(methodName).append(":").append(parameterTypes.length);
        for (Class<?> p : parameterTypes) {
            keyBuilder.append(":").append(p == null ? "null" : p.getName());
        }
        String key = keyBuilder.toString();
        Method cached = methodCache.get(key);
        if (cached != null) {
            return cached;
        }
        for (Class<?> searchType = cls; searchType != null && searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                methodCache.putIfAbsent(key, method);
                return method;
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    /**
     * 更智能的 getAccessibleMethodByName：根据参数实际类型（args）在所有同名方法中选择最合适的重载。
     * 使用运行时参数类型签名为缓存 key，避免不同类型但相同参数数量的冲突。
     * 如果 args 为 null，仅根据参数数量匹配（缓存 key 使用参数数量）。
     * 支持 varargs 方法的匹配尝试与选择。
     * 在 score 相同的情况下，使用更细粒度的 tiebreaker：
     * - 首先比较方法参数相对于调用方参数的“总距离”（越小越好）
     * - 若仍相等，比较两个方法参数之间的“更具体性”（参数类型是另一个参数类型的子类型，则更具体）
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName, int argsNum, Object[] args) {
        if (obj == null) {
            return null;
        }
        Validate.notBlank(methodName, "方法名不能为空");
        Class<?> cls = obj.getClass();

        // 构造缓存 key：类名#methodName:argCount:argType1,argType2,...
        String cacheKey;
        if (args == null) {
            cacheKey = cls.getName() + "#" + methodName + ":" + argsNum;
        } else {
            StringBuilder sb = new StringBuilder(cls.getName()).append("#").append(methodName).append(":").append(argsNum).append(":");
            for (int i = 0; i < args.length; i++) {
                Object a = args[i];
                sb.append(i == 0 ? "" : ",").append(a == null ? "null" : a.getClass().getName());
            }
            cacheKey = sb.toString();
        }

        Method cached = methodCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        Method best = null;
        int bestScore = Integer.MIN_VALUE;
        long bestDistance = Long.MAX_VALUE; // tiebreaker: smaller total distance better

        for (Class<?> searchType = cls; searchType != null && searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method m : methods) {
                if (!m.getName().equals(methodName)) continue;
                Class<?>[] pts = m.getParameterTypes();

                // quick length check: if not varargs and length != argsNum, skip
                if (!m.isVarArgs() && pts.length != argsNum) continue;

                int score = scoreMethodMatch(pts, args, m.isVarArgs());
                if (score < Integer.MIN_VALUE / 4) continue; // incompatible

                long dist = computeTotalDistance(pts, args, m.isVarArgs());

                if (score > bestScore || (score == bestScore && dist < bestDistance)
                        || (score == bestScore && dist == bestDistance && isMethodMoreSpecific(m, best))) {
                    bestScore = score;
                    best = m;
                    bestDistance = dist;
                }
            }
            if (bestScore == Integer.MAX_VALUE) break; // perfect match found
        }

        if (best != null) {
            makeAccessible(best);
            methodCache.putIfAbsent(cacheKey, best);
        }
        return best;
    }

    /**
     * 计算方法参数对实际调用参数的“总距离”：距离越小表示参数类型越接近调用参数类型。
     * 若参数为 null，则给出中性距离 1（可接受但不完美）。
     * 不兼容时返回一个大数。
     */
    private static long computeTotalDistance(Class<?>[] paramTypes, Object[] args, boolean isVarArgs) {
        long total = 0L;
        int argLen = args == null ? 0 : args.length;
        if (isVarArgs) {
            int fixed = paramTypes.length - 1;
            if (argLen < fixed) return Long.MAX_VALUE / 4;
            // fixed part
            for (int i = 0; i < fixed; i++) {
                total += singleDistance(paramTypes[i], args[i]);
            }
            Class<?> varComp = paramTypes[fixed].getComponentType();
            for (int j = fixed; j < argLen; j++) {
                total += singleDistance(varComp, args[j]);
            }
        } else {
            if (paramTypes.length != argLen) return Long.MAX_VALUE / 4;
            for (int i = 0; i < paramTypes.length; i++) {
                total += singleDistance(paramTypes[i], args[i]);
            }
        }
        return total;
    }

    /**
     * 计算单个参数类型与实际 arg 的距离：越小越好。不可赋值返回较大值（表示不兼容）。
     */
    private static long singleDistance(Class<?> paramType, Object arg) {
        if (arg == null) {
            return paramType.isPrimitive() ? Long.MAX_VALUE / 4 : 1L;
        }
        Class<?> from = arg.getClass();
        if (isAssignable(from, paramType)) {
            // 计算从 'from' 向上到 paramType 的步数（类继承 + interface BFS）
            return inheritanceDistance(from, paramType);
        }
        // 可转换（但非直接 assignable）为较大代价
        Class<?> boxed = boxIfPrimitive(paramType);
        if (Number.class.isAssignableFrom(boxed) && (Number.class.isAssignableFrom(from) || from == String.class)) {
            return 50L;
        }
        if (String.class.equals(boxed)) {
            return 30L;
        }
        if (paramType.isEnum() && from == String.class) {
            return 40L;
        }
        // BigDecimal 从 Number/String 转换也可接受
        if (BigDecimal.class.equals(boxed) && (Number.class.isAssignableFrom(from) || from == String.class)) {
            return 45L;
        }
        // byte[] 从 byte[] 或 String 或 Number 可接受（Number -> single-byte array）
        if (paramType.isArray() && paramType.getComponentType() == byte.class) {
            if (from == String.class || from == byte[].class) return 35L;
            if (Number.class.isAssignableFrom(from) || from == Byte.class) return 40L;
        }
        // Character from String is acceptable (treat as moderate distance)
        if ((Character.class.equals(boxed) || char.class.equals(paramType)) && from == String.class) {
            String s = (String) arg;
            return !s.isEmpty() ? 30L : Long.MAX_VALUE / 4;
        }
        return Long.MAX_VALUE / 4;
    }

    /**
     * 计算 from -> to 的继承/接口距离（最小步数）。如果不是可赋值关系，返回一个大数。
     */
    private static long inheritanceDistance(Class<?> from, Class<?> to) {
        if (from.equals(to)) return 0L;
        // BFS up the type graph (classes and interfaces)
        long dist = 0L;
        Class<?> cls = from;
        // climb classes chain first
        while (cls != null) {
            if (cls.equals(to)) return dist;
            // check interfaces at this level via BFS
            long ifaceDist = interfaceDistance(cls, to);
            if (ifaceDist >= 0) return dist + ifaceDist;
            cls = cls.getSuperclass();
            dist++;
        }
        return Long.MAX_VALUE / 8;
    }

    private static long interfaceDistance(Class<?> cls, Class<?> target) {
        // BFS on interface graph
        if (cls == null) return -1;
        if (cls.equals(target)) return 0;
        Class<?>[] ifs = cls.getInterfaces();
        if (ifs.length == 0) return -1;
        long min = Long.MAX_VALUE / 8;
        for (Class<?> ifc : ifs) {
            if (ifc.equals(target)) return 1;
            long d = interfaceDistanceRec(ifc, target, 1);
            if (d > 0 && d < min) min = d;
        }
        return min == Long.MAX_VALUE / 8 ? -1 : min;
    }

    private static long interfaceDistanceRec(Class<?> iface, Class<?> target, long depth) {
        if (iface.equals(target)) return depth;
        Class<?>[] ifs = iface.getInterfaces();
        if (ifs.length == 0) return -1;
        long min = Long.MAX_VALUE / 8;
        for (Class<?> ifc : ifs) {
            long d = interfaceDistanceRec(ifc, target, depth + 1);
            if (d > 0 && d < min) min = d;
        }
        return min == Long.MAX_VALUE / 8 ? -1 : min;
    }

    /**
     * 在两方法得分相同且距离相同的情况下，判定某个方法参数是否比另一个更"具体"（即参数类型是另一个参数类型的子类型）
     * 若 best 为 null，返回 true（当前方法更优）。
     */
    private static boolean isMethodMoreSpecific(Method cur, Method best) {
        if (best == null) return true;
        Class<?>[] a = cur.getParameterTypes();
        Class<?>[] b = best.getParameterTypes();
        int len = Math.min(a.length, b.length);
        int curMore = 0;
        int bestMore = 0;
        for (int i = 0; i < len; i++) {
            if (a[i].equals(b[i])) continue;
            if (a[i].isAssignableFrom(b[i])) bestMore++;
            else if (b[i].isAssignableFrom(a[i])) curMore++;
        }
        return curMore > bestMore;
    }

    /**
     * 为方法匹配计算评分（数值越高越匹配）。负分表示不兼容。
     */
    private static int scoreMethodMatch(Class<?>[] paramTypes, Object[] args, boolean isVarArgs) {
        int score = 0;
        int argLen = args == null ? 0 : args.length;

        if (isVarArgs) {
            int fixed = paramTypes.length - 1;
            if (argLen < fixed) return Integer.MIN_VALUE / 2; // 参数过少不能调用（除非 varargs 用空数组，但我们要求至少 fixed 个）
            // 评估固定参数
            for (int i = 0; i < fixed; i++) {
                Object a = args[i];
                int s = scoreSingle(paramTypes[i], a);
                if (s < 0) return Integer.MIN_VALUE / 2;
                score += s;
            }
            // 评估 varargs 部分
            Class<?> varComp = paramTypes[fixed].getComponentType();
            for (int j = fixed; j < argLen; j++) {
                Object a = args[j];
                int s = scoreSingle(varComp, a);
                if (s < 0) return Integer.MIN_VALUE / 2;
                score += s;
            }
        } else {
            if (paramTypes.length != argLen) return Integer.MIN_VALUE / 2;
            for (int i = 0; i < paramTypes.length; i++) {
                Object a = args[i];
                int s = scoreSingle(paramTypes[i], a);
                if (s < 0) return Integer.MIN_VALUE / 2;
                score += s;
            }
        }
        return score;
    }

    /**
     * 单参数评分：完全匹配得 10；可赋值得 7；可转换得 3；不兼容得 -1。
     */
    private static int scoreSingle(Class<?> paramType, Object arg) {
        if (arg == null) {
            return paramType.isPrimitive() ? -1 : 3;
        }
        Class<?> from = arg.getClass();
        if (isAssignable(from, paramType)) {
            return 10;
        }
        Class<?> boxed = boxIfPrimitive(paramType);
        String name = boxed.getName();
        switch (name) {
            case "java.lang.String" -> {
                return 3;
            }
            case "java.lang.Integer", "java.lang.Long", "java.lang.Double", "java.lang.Float", "java.lang.Boolean" -> {
                if (Number.class.isAssignableFrom(from) || from == String.class || from == Boolean.class) {
                    return 3;
                }
                return -1;
            }
            case "java.util.Date" -> {
                if (from == String.class || Number.class.isAssignableFrom(from)) return 3;
                return -1;
            }
            case "java.lang.Character" -> {
                // Accept a single-character String or a Character
                if (from == Character.class) return 10;
                if (from == String.class) {
                    String s = (String) arg;
                    return !s.isEmpty() ? 3 : -1;
                }
                return -1;
            }
            default -> {
                // byte[] target: accept byte[]/String and Number (as single-byte array)
                if (paramType.isArray() && paramType.getComponentType() == byte.class) {
                    if (from == byte[].class || from == String.class) return 3;
                    if (Number.class.isAssignableFrom(from) || from == Byte.class) return 3;
                }
                if (paramType.isEnum() && from == String.class) return 3;
                if (BigDecimal.class.equals(boxed) && (Number.class.isAssignableFrom(from) || from == String.class)) return 3;
                return -1;
            }
        }
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     * 使用 trySetAccessible() 优先尝试，不可用时记录日志。
     */
    public static void makeAccessible(Method method) {
        if (method == null) {
            return;
        }
        try {
            boolean need = (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()));
            if (need) {
                boolean ok = method.trySetAccessible();
                if (!ok) {
                    logger.warn("无法将方法设为可访问: {}.{} (trySetAccessible 返回 false)", method.getDeclaringClass(), method.getName());
                }
            }
        } catch (Throwable t) {
            logger.warn("尝试将方法设为可访问时发生异常: {}#{} : {}", method.getDeclaringClass(), method.getName(), t.getMessage());
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     * 使用 trySetAccessible() 优先尝试，不可用时记录日志。
     */
    public static void makeAccessible(Field field) {
        if (field == null) {
            return;
        }
        try {
            boolean need = (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                    || Modifier.isFinal(field.getModifiers()));
            if (need) {
                boolean ok = field.trySetAccessible();
                if (!ok) {
                    logger.warn("无法将字段设为可访问: {}.{} (trySetAccessible 返回 false)", field.getDeclaringClass(), field.getName());
                }
            }
        } catch (Throwable t) {
            logger.warn("尝试将字段设为可访问时发生异常: {}#{} : {}", field.getDeclaringClass(), field.getName(), t.getMessage());
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class<?> clazz) {
        return (Class<T>) getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     */
    public static Class<?> getClassGenricType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.debug("{} 的父类没有设置泛型参数 ", clazz.getSimpleName());
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.debug("Index索引: {}, {} 的泛型参数个数为: {} ", index, clazz.getSimpleName(), params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.debug("{} 沒有设置真正的类作为泛型参数 ", clazz.getSimpleName());
            return Object.class;
        }

        return (Class<?>) params[index];
    }

    public static Class<?> getUserClass(Object instance) {
        Objects.requireNonNull(instance, "實例不能為空");
        Class<?> clazz = instance.getClass();
        if (clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(String msg, Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(msg, e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(msg, ((InvocationTargetException) e).getTargetException());
        }
        return new RuntimeException(msg, e);
    }
}

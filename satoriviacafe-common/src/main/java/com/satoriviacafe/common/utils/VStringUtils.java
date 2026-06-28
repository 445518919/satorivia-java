package com.satoriviacafe.common.utils;

import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.core.text.StrFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.INDEX_NOT_FOUND;
import static org.apache.commons.lang3.Strings.CI;
import static org.apache.commons.lang3.Strings.CS;

/**
 * 字符串工具类
 *
 * @author satoriviacafe
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VStringUtils {
    /**
     * 空字符串
     */
    public static final String EMPTY = StringUtils.EMPTY;

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 星号
     */
    private static final char ASTERISK = '*';

    /**
     * 判断字符串是否为空白，空白的定义如下：<br>
     * 1. null<br>
     * 2. ""<br>
     * 3. 全部由空白字符(whitespace)构成<br>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回true
     */
    public static boolean isBlank(CharSequence str) {
        return StringUtils.isBlank(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }


    /**
     * 相等判断(大小寫敏感)
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return 结果
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        return CS.equals(cs1, cs2);
    }

    /**
     * 相等判断(大小寫不敏感)
     *
     * @param cs1 字符串1
     * @param cs2 字符串2
     * @return 结果
     */
    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        return CI.equals(cs1, cs2);
    }

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个CharSequence是否为空
     *
     * @param cs 要判断的CharSequence
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.isEmpty();
    }


    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || EMPTY.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 替换指定字符串的指定区间内字符为"*"
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @return 替换后的字符串
     */
    public static String hide(CharSequence str, int startInclude, int endExclude) {
        if (isEmpty(str)) {
            return EMPTY;
        }
        final int strLength = str.length();
        if (startInclude > strLength) {
            return EMPTY;
        }
        if (endExclude > strLength) {
            endExclude = strLength;
        }
        if (startInclude > endExclude) {
            // 如果起始位置大于结束位置，不替换
            return EMPTY;
        }
        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++) {
            if (i >= startInclude && i < endExclude) {
                chars[i] = ASTERISK;
            } else {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return EMPTY;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return EMPTY;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 截取字符串 str 中第一个出现的 separator 之前的内容
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 结果
     */
    public static String substringBefore(final String str, final String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    /**
     * 截取字符串 str 中第一个出现的 separator 之后的内容
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return 结果
     */
    public static String substringAfter(final String str, final String separator) {
        return StringUtils.substringAfter(str, separator);
    }

    /**
     * 在字符串中查找第一个出现的 `open` 和最后一个出现的 `close` 之间的子字符串
     *
     * @param str   要截取的字符串
     * @param open  起始字符串
     * @param close 结束字符串
     * @return 截取结果
     */
    public static String substringBetweenLast(final String str, final String open, final String close) {
        if (isEmpty(str) || isEmpty(open) || isEmpty(close)) {
            return EMPTY;
        }
        final int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            final int end = str.lastIndexOf(close);
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return EMPTY;
    }

    /**
     * 判断是否为空，并且不是空白字符
     *
     * @param str 要判断的value
     * @return 结果
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean ishttp(String link) {
        return CS.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     * 字符串转set
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return set集合
     */
    public static Set<String> str2Set(String str, String regex) {
        return new HashSet<>(str2List(str, regex, true, false));
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param regex       正则表达式
     * @param filterBlank 过滤纯空白
     * @param trim        去掉首尾空白
     * @return list集合
     */
    public static List<String> str2List(String str, String regex, boolean filterBlank, boolean trim) {
        List<String> list = new ArrayList<>();
        if (VStringUtils.isEmpty(str)) {
            return list;
        }

        // 过滤空白字符串
        if (filterBlank && isBlank(str)) {
            return list;
        }
        String[] split = str.split(regex);
        for (String string : split) {
            if (filterBlank && isBlank(string)) {
                continue;
            }
            if (trim) {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * 查找指定字符串是否包含指定字符串
     *
     * @param cs         指定字符串
     * @param searchChar 需要检查的字符串
     * @return 是否包含
     */
    public static boolean contains(final CharSequence cs, final CharSequence searchChar) {
        return CS.contains(cs, searchChar);
    }

    /**
     * 查找指定字符串是否包含指定字符串忽略大小写
     *
     * @param cs         指定字符串
     * @param searchChar 需要检查的字符串
     * @return 是否包含
     */
    public static boolean containsIgnoreCase(final CharSequence cs, final CharSequence searchChar) {
        return CI.contains(cs, searchChar);
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
     *
     * @param cs                  指定字符串
     * @param searchCharSequences 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAny(final CharSequence cs, final CharSequence... searchCharSequences) {
        return CS.containsAny(cs, searchCharSequences);
    }

    /**
     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
     *
     * @param collection 给定的集合
     * @param array      给定的数组
     * @return boolean 结果
     */
    public static boolean containsAny(Collection<String> collection, String... array) {
        if (isEmpty(collection) || isEmpty(array)) {
            return false;
        }
        for (String str : array) {
            if (collection.contains(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串同时串忽略大小写
     *
     * @param cs                  指定字符串
     * @param searchCharSequences 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
        if (isEmpty(cs) || isEmpty(searchCharSequences)) {
            return false;
        }
        for (CharSequence testStr : searchCharSequences) {
            if (CI.contains(cs, testStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value忽略大小写
     *
     * @param collection 给定的集合
     * @param array      给定的数组
     * @return boolean 结果
     */
    public static boolean containsAnyIgnoreCase(Collection<String> collection, String... array) {
        if (isEmpty(collection) || isEmpty(array)) {
            return false;
        }
        for (String str : array) {
            for (String collStr : collection) {
                if (CI.equals(str, collStr)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串是否以指定字符串开头
     *
     * @param str    字符串
     * @param prefix 指定字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
        return CS.startsWith(str, prefix);
    }

    /**
     * 字符串是否以指定字符串开头忽略大小写
     *
     * @param str    字符串
     * @param prefix 指定字符串
     * @return 是否以指定字符串开头忽略大小写
     */
    public static boolean startsWithIgnoreCase(final CharSequence str, final CharSequence prefix) {
        return CI.startsWith(str, prefix);
    }

    public static boolean startsWithAny(final CharSequence sequence, final CharSequence... searchStrings) {
        return Strings.CS.startsWithAny(sequence, searchStrings);
    }


    /**
     * 字符串是否以指定字符串结尾
     *
     * @param str    字符串
     * @param suffix 指定字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
        return CS.endsWith(str, suffix);
    }

    /**
     * 字符串是否以指定字符串结尾忽略大小写
     *
     * @param str    字符串
     * @param suffix 指定字符串
     * @return 是否以指定字符串结尾忽略大小写
     */
    public static boolean endsWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
        return CI.endsWith(str, suffix);
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase;
        // 当前字符是否大写
        boolean currCharIsUpperCase;
        // 下一字符是否大写
        boolean nextCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            currCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nextCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && currCharIsUpperCase && !nextCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && currCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法
     * 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1) {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num  数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static String padl(final Number num, final int size) {
        if (num == null) {
            return padl(EMPTY, size, '0');
        }
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s    原始字符串
     * @param size 字符串指定长度
     * @param c    用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static String padl(final String s, final int size, final char c) {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            final int len = s.length();
            if (s.length() <= size) {
                sb.append(String.valueOf(c).repeat(size - len));
                sb.append(s);
            } else {
                return s.substring(len - size, len);
            }
        } else {
            sb.append(String.valueOf(c).repeat(Math.max(0, size)));
        }
        return sb.toString();
    }

    /**
     * 删除字符串头尾部的指定字符串
     *
     * @param str        原始字符串
     * @param stripChars 要删除的字符串
     * @return 删除后的字符串
     */
    public static String stripEnd(final String str, final String stripChars) {
        return StringUtils.stripEnd(str, stripChars);
    }

    /**
     * 重复某个字符
     *
     * @param repeat 要重复的字符
     * @param count  重复次数
     * @return 重复指定字符count次后的字符串
     */
    public static String repeat(final char repeat, final int count) {
        return StringUtils.repeat(repeat, count);
    }

    /**
     * 数组转字符串
     *
     * @param array     数组
     * @param delimiter 分隔符
     * @return 字符串
     */
    public static String join(final Object[] array, final char delimiter) {
        return StringUtils.join(array, delimiter);
    }

    /**
     * 数组转字符串
     *
     * @param array     数组
     * @param delimiter 分隔符
     * @return 字符串
     */
    public static String join(final Object[] array, final String delimiter) {
        return StringUtils.join(array, delimiter);
    }

    /**
     * 数组转字符串
     *
     * @param elements 数组
     * @return 字符串
     */
    @SafeVarargs
    public static <T> String join(final T... elements) {
        return join(elements, null);
    }

    /**
     * 是否为数字
     *
     * @param cs 字符串
     * @return 结果
     */
    public static boolean isNumeric(final CharSequence cs) {
        return StringUtils.isNumeric(cs);
    }

    /**
     * 字符串长度
     *
     * @param cs 字符串
     * @return 长度
     */
    public static int length(CharSequence cs) {
        return StringUtils.length(cs);
    }

    /**
     * 返回指定字符串在字符串中首次出现的位置，从0开始计数。
     * 如果未找到该字符串，则返回-1。
     *
     * @param seq       字符串
     * @param searchSeq 需要查找的字符串
     * @return 位置
     */
    public static int indexOf(final CharSequence seq, final CharSequence searchSeq) {
        return CS.indexOf(seq, searchSeq);
    }

    /**
     * 返回指定字符串在字符串中首次出现的位置，从0开始计数。
     * 忽略大小写。如果未找到该字符串，则返回-1。
     *
     * @param str       字符串
     * @param searchStr 需要查找的字符串
     * @return 位置
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return CI.indexOf(str, searchStr);
    }


    public static String[] split(final String str, final String separatorChars) {
        return StringUtils.split(str, separatorChars);
    }


    public static String defaultString(final String str) {
        return StringUtils.defaultString(str);
    }

    public static int countMatches(final CharSequence str, final CharSequence sub) {
        return StringUtils.countMatches(str, sub);
    }

    public static String substringBeforeLast(final String str, final String separator) {
        return StringUtils.substringBeforeLast(str, separator);
    }

    public static String substringAfterLast(final String str, final String separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    public static String trimToEmpty(final String str) {
        return StringUtils.trimToEmpty(str);
    }

    public static String substringBetween(final String str, final String open, final String close) {
        return StringUtils.substringBetween(str, open, close);
    }

    public static String capitalize(final String str) {
        return StringUtils.capitalize(str);
    }

    public static String uncapitalize(final String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String replace(final String text, final String searchString, final String replacement) {
        return CS.replace(text, searchString, replacement);
    }

    public static String replaceIgnoreCase(final String text, final String searchString, final String replacement) {
        return CI.replace(text, searchString, replacement);
    }

    public static String replaceEach(final String text, final String[] searchList, final String[] replacementList) {
        return StringUtils.replaceEach(text, searchList, replacementList);
    }

    public static boolean equalsAny(final CharSequence string, final CharSequence... searchStrings) {
        return CS.equalsAny(string, searchStrings);
    }

    public static boolean equalsAnyIgnoreCase(final CharSequence string, final CharSequence... searchStrings) {
        return CI.equalsAny(string, searchStrings);
    }

    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return StringUtils.defaultIfEmpty(str, defaultStr);
    }

    public static String lowerCase(final String str) {
        return StringUtils.lowerCase(str);
    }

    public static String removeStart(final String str, final CharSequence remove) {
        return CS.removeStart(str, remove);
    }

    public static String removeStartIgnoreCase(final String str, final CharSequence remove) {
        return CI.removeStart(str, remove);
    }

    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes(StandardCharsets.UTF_8));
    }
}

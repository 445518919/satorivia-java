package com.satoriviacafe.generator.util;

import com.satoriviacafe.common.constant.GenConstants;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.generator.config.GenConfig;
import com.satoriviacafe.generator.domain.GenTable;
import com.satoriviacafe.generator.domain.GenTableColumn;
import org.apache.commons.lang3.RegExUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.Strings.CI;
import static org.apache.commons.lang3.Strings.CS;

/**
 * 代码生成器 工具类
 *
 * @author satoriviacafe
 */
public class GenUtils {
    /**
     * 初始化表信息
     */
    public static void initTable(GenTable genTable, String operName) {
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setPackageName(GenConfig.getPackageName());
        genTable.setModuleName(getModuleName(GenConfig.getPackageName()));
        genTable.setBusinessName(getBusinessName(genTable.getTableName()));
        genTable.setFunctionName(replaceText(genTable.getTableComment()));
        genTable.setFunctionAuthor(GenConfig.getAuthor());
        genTable.setCreateBy(operName);
    }

    private static final String UNDERSCORE = "_";
    private static final String SUFFIX_I18N = "_i18n";
    private static final String SUFFIX_LOG = "_log";
    private static final String SUFFIX_RECODE = "_record";

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return VStringUtils.substring(packageName, lastIndex + 1, nameLength);
    }
    // 如果将来有更多后缀，可以在这里添加，并加入到 SUFFIXES 列表中
    // 例如: private static final String SUFFIX_AUDIT = "_audit";

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumn column, GenTable table) {
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getTableId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(VStringUtils.toCamelCase(columnName));
        // 设置默认类型
        column.setJavaType(GenConstants.TYPE_STRING);
        column.setQueryType(GenConstants.QUERY_EQ);

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType)) {
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(column.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            column.setHtmlType(htmlType);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
            column.setHtmlType(GenConstants.HTML_DATETIME);
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            column.setHtmlType(GenConstants.HTML_INPUT);

            // 如果是浮点型 统一用BigDecimal
            String[] str = VStringUtils.split(VStringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                column.setJavaType(GenConstants.TYPE_BIGDECIMAL);
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                column.setJavaType(GenConstants.TYPE_INTEGER);
            }
            // 长整形
            else {
                column.setJavaType(GenConstants.TYPE_LONG);
            }
        }

        // 插入字段（默认所有字段都需要插入）
        column.setIsInsert(GenConstants.REQUIRE);

        // 编辑字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPk()) {
            column.setIsEdit(GenConstants.REQUIRE);
        }
        // 列表字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPk()) {
            column.setIsList(GenConstants.REQUIRE);
        }
        // 查询字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk()) {
            column.setIsQuery(GenConstants.REQUIRE);
        }

        // 查询字段类型
        if (CI.endsWith(columnName, "name")) {
            column.setQueryType(GenConstants.QUERY_LIKE);
        }
        // 状态字段设置单选框
        if (CI.endsWith(columnName, "status")) {
            column.setHtmlType(GenConstants.HTML_RADIO);
        }
        // 类型&性别字段设置下拉框
        else if (CI.endsWith(columnName, "type")
                || CI.endsWith(columnName, "sex")) {
            column.setHtmlType(GenConstants.HTML_SELECT);
        }
        // 图片字段设置图片上传控件
        else if (CI.endsWith(columnName, "image") ||
                CI.endsWith(columnName, "logo") ||
                CI.endsWith(columnName, "avatar") ||
                CI.endsWith(columnName, "icon") ||
                CI.endsWith(columnName, "banner")) {
            column.setHtmlType(GenConstants.HTML_IMAGE_UPLOAD);
        }
        // 文件字段设置文件上传控件
        else if (CI.endsWith(columnName, "file")) {
            column.setHtmlType(GenConstants.HTML_FILE_UPLOAD);
        }
        // 内容字段设置富文本控件
        else if (CI.endsWith(columnName, "content") || CI.endsWith(columnName, "description")) {
            column.setHtmlType(GenConstants.HTML_EDITOR);
        }
    }
    // 定义后缀列表，顺序很重要，它决定了移除和重新添加的顺序
    private static final List<String> SUFFIXES = Arrays.asList(SUFFIX_I18N, SUFFIX_LOG,SUFFIX_RECODE);
    // 如果添加了 SUFFIX_AUDIT, 可以这样:
    // private static final List<String> SUFFIXES = Arrays.asList(SUFFIX_I18N, SUFFIX_LOG, SUFFIX_AUDIT);

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        if (VStringUtils.isEmpty(tableName)) {
            return "";
        }

        String moduleName = getModuleName(GenConfig.getPackageName());
        // 1. 拆分所有下划线分隔片段
        String[] parts = tableName.split(UNDERSCORE);
        // 2. 移除所有等于 moduleName 的片段
        StringBuilder filtered = new StringBuilder();
        for (String part : parts) {
            if (!part.equals(moduleName)) {
                if (!filtered.isEmpty()) {
                    filtered.append(UNDERSCORE);
                }
                filtered.append(part);
            }
        }

        // 3. 后缀处理
        String currentTableName = filtered.toString();
        String[] capturedSuffixesArray = new String[SUFFIXES.size()];
        Arrays.fill(capturedSuffixesArray, "");

        for (int i = 0; i < SUFFIXES.size(); i++) {
            String suffix = SUFFIXES.get(i);
            if (currentTableName.endsWith(suffix)) {
                capturedSuffixesArray[i] = suffix;
                currentTableName = currentTableName.substring(0, currentTableName.length() - suffix.length());
            }
        }

        // 4. 取第一个下划线后的部分作为 base
        StringBuilder resultBuilder = getResultBuilder(currentTableName, capturedSuffixesArray);
        return resultBuilder.toString();
    }

    @NotNull
    private static StringBuilder getResultBuilder(String currentTableName, String[] capturedSuffixesArray) {
        String baseNamePart;
        int firstUnderscoreIndex = currentTableName.indexOf('_');
        if (firstUnderscoreIndex != -1 && firstUnderscoreIndex < currentTableName.length() - 1) {
            baseNamePart = currentTableName.substring(firstUnderscoreIndex + 1);
        } else if (firstUnderscoreIndex == -1) {
            baseNamePart = currentTableName;
        } else {
            baseNamePart = "";
        }

        // 5. 拼接最终结果
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(baseNamePart);
        for (String capturedSuffix : capturedSuffixesArray) {
            resultBuilder.append(capturedSuffix);
        }
        return resultBuilder;
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName 表名称
     * @return 类名
     */
    public static String convertClassName(String tableName) {
        boolean autoRemovePre = GenConfig.getAutoRemovePre();
        String tablePrefix = GenConfig.getTablePrefix();
        if (autoRemovePre && VStringUtils.isNotEmpty(tablePrefix)) {
            String[] searchList = VStringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return VStringUtils.convertToCamelCase(tableName);
    }

    /**
     * 批量替换前缀
     *
     * @param replacement 替换值
     * @param searchList   替换列表
     * @return 替换后的字符串
     */
    public static String replaceFirst(String replacement, String[] searchList) {
        String text = replacement;
        for (String searchString : searchList) {
            if (replacement.startsWith(searchString)) {
                text = replacement.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表|Satoriviacafe)", "");
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType) {
        if (CS.indexOf(columnType, "(") > 0) {
            return VStringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType) {
        if (CS.indexOf(columnType, "(") > 0) {
            String length = VStringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }
}

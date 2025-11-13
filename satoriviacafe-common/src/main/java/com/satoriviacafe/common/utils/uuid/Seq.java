package com.satoriviacafe.common.utils.uuid;

import com.satoriviacafe.common.utils.DateUtils;
import com.satoriviacafe.common.utils.VStringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author satoriviacafe 序列生成类
 */
public class Seq {

    // 通用序列类型
    public static final String commSeqType = "COMMON";

    // 上传序列类型
    public static final String uploadSeqType = "UPLOAD";

    // 通用接口序列数
    private static final AtomicInteger commSeq = new AtomicInteger(1);

    // 上传接口序列数
    private static final AtomicInteger uploadSeq = new AtomicInteger(1);

    // 机器标识
    private static final String machineCode = "A";

    /**
     * 获取通用序列号
     *
     * @return 序列值
     */
    public static String getId() {
        return getId(commSeqType);
    }

    /**
     * 默认16位序列号 yyMMddHHmmss + 一位机器标识 + 3长度循环递增字符串
     *
     * @return 序列值
     */
    public static String getId(String type) {
        AtomicInteger atomicInt = commSeq;
        if (uploadSeqType.equals(type)) {
            atomicInt = uploadSeq;
        }
        return getId(atomicInt, 3);
    }

    /**
     * 通用接口序列号 yyMMddHHmmss + 一位机器标识 + length长度循环递增字符串
     *
     * @param atomicInt 序列数
     * @param length    数值长度
     * @return 序列值
     */
    public static String getId(AtomicInteger atomicInt, int length) {
        String result = DateUtils.dateTimeNow();
        result += machineCode;
        result += getSeq(atomicInt, length);
        return result;
    }

    /**
     * 序列循环递增字符串[1, 10 的 (length)幂次方), 用0左补齐length位数
     *
     * @return 序列值
     */
    private static String getSeq(AtomicInteger atomicInt, int length) {
        int maxSeq = (int) Math.pow(10, length);
        int value;
        // 无锁自旋，先读后更新，下溢时回到1
        do {
            value = atomicInt.get();
            int next = value + 1;
            if (next >= maxSeq) next = 1;
            // 如果 CAS 成功就跳出
            if (atomicInt.compareAndSet(value, next)) {
                break;
            }
        } while (true);
        return VStringUtils.padl(value, length);
    }
}

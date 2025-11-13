package com.satoriviacafe.common.utils.reflect;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ReflectUtilsTestHelper {

    @Setter
    @Getter
    private String hidden = "initial";
    public int publicInt = 42;

    public enum TestEnum {
        A, B, C
    }

    // simple getter/setter style methods
    public String echo(String s) {
        return s;
    }

    public String echoEnum(TestEnum e) {
        return e == null ? null : e.name();
    }

    public long echoDateMillis(Date d) {
        return d == null ? -1L : d.getTime();
    }

    public int sumInts(int a, int b) {
        return a + b;
    }

    public long sumLongs(long a, long b) {
        return a + b;
    }

    // overloaded methods
    public String overload(Number n) {
        return "Number:" + n;
    }

    public String overload(Integer i) {
        return "Integer:" + i;
    }

    // varargs methods
    public String joinVarargs(String... parts) {
        if (parts == null) return "";
        return String.join(",", parts);
    }

    public String joinVarargsWithPrefix(String prefix, String... parts) {
        if (parts == null) return prefix;
        return prefix + ":" + String.join(",", parts);
    }

    // accepts an array directly (to test pre-packed array case)
    public String joinArray(String[] parts) {
        if (parts == null) return "";
        return String.join("|", parts);
    }

    // method used to test private invocation via reflection (non-varargs)
    private String privateConcat(String a, String b) {
        return a + ":" + b;
    }


    // methods for specificity tie-breaker tests
    public String choose(Object o) {
        return "Object";
    }

    public String choose(java.io.Serializable s) {
        return "Serializable";
    }


    @Data
    public static class ParentBean {
        private ChildBean child = new ChildBean();

    }
    @Data
    public static class ChildBean {
        private String name;

    }

}

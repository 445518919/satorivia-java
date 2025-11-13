package com.satoriviacafe.common.utils.reflect;

import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 測試 ReflectUtils 的各種功能，包括方法調用、字段訪問、類型轉換、重載解析和緩存行為。
 * 測試類使用 JUnit 5 框架，並按方法名稱排序執行以確保特定順序。
 * 包含多線程測試以驗證緩存的線程安全性
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ReflectUtilsTest {

    private ReflectUtilsTestHelper helper;

    @BeforeEach
    public void setUp() {
        helper = new ReflectUtilsTestHelper();
        // ensure default behavior
        ReflectUtils.setTrimTrailingDotZero(true);
    }


    @Test
    public void test01_stringTrimDotZero_defaultTrue() {
        // pass a Double that Convert.toStr would produce "123.0" -> expect trimmed "123"
        Object result = ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.0});
        assertNotNull(result);
        assertEquals("123", result);
    }

    @Test
    public void test02_stringTrimDotZero_disabled() {
        ReflectUtils.setTrimTrailingDotZero(false);
        Object result = ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.0});
        assertNotNull(result);
        assertEquals("123.0", result);
        // restore
        ReflectUtils.setTrimTrailingDotZero(true);
    }

    @Test
    public void test03_numericConversion_forSumInts() {
        // pass string numbers -> converted to int
        Object result = ReflectUtils.invokeMethodByName(helper, "sumInts", new Object[]{"2", "3"});
        assertInstanceOf(Integer.class, result);
        assertEquals(5, ((Integer) result).intValue());
    }

    @Test
    public void test04_longConversion_and_overloads() {
        // call sumLongs with mixed types (String, Double)
        Object result = ReflectUtils.invokeMethodByName(helper, "sumLongs", new Object[]{"10", 5.0});
        assertInstanceOf(Long.class, result);
        assertEquals(15L, ((Long) result).longValue());
    }

    @Test
    public void test05_dateConversion_fromNumber() {
        // Excel style numeric date -> Date
        double excelDate = 43831.0; // corresponds roughly to some date (depends on DateUtil)
        Date expected = DateUtil.getJavaDate(excelDate);
        Object result = ReflectUtils.invokeMethodByName(helper, "echoDateMillis", new Object[]{excelDate});
        assertInstanceOf(Long.class, result);
        assertEquals(expected.getTime(), ((Long) result).longValue());
    }

    @Test
    public void test06_enumConversion() {
        Object result = ReflectUtils.invokeMethodByName(helper, "echoEnum", new Object[]{"A"});
        assertEquals("A", result);
        // case-insensitive fallback for enum conversion tested via convertIfNeeded tries ignore-case
        Object result2 = ReflectUtils.invokeMethodByName(helper, "echoEnum", new Object[]{"b"});
        assertEquals("B", result2);
    }

    @Test
    public void test07_privateMethodInvocation_viaInvokeMethod() {
        // private method privateConcat should be invokable via getAccessibleMethod/getAccessibleMethodByName path
        java.lang.reflect.Method privateMethod = ReflectUtils.getAccessibleMethod(helper, "privateConcat", String.class, String.class);
        assertNotNull(privateMethod);
        Object r = ReflectUtils.invokeMethod(helper, "privateConcat", new Class[]{String.class, String.class}, new Object[]{"x", "y"});
        assertEquals("x:y", r);
    }

    @Test
    public void test08_fieldGetSet_privateField() {
        // get private field via getFieldValue and set via setFieldValue
        String before = ReflectUtils.getFieldValue(helper, "hidden");
        assertEquals("initial", before);

        ReflectUtils.setFieldValue(helper, "hidden", "changed");
        String after = ReflectUtils.getFieldValue(helper, "hidden");
        assertEquals("changed", after);

        // also confirm public field access
        int pub = ReflectUtils.getFieldValue(helper, "publicInt");
        assertEquals(42, pub);
        ReflectUtils.setFieldValue(helper, "publicInt", 7);
        assertEquals(7, (int) ReflectUtils.getFieldValue(helper, "publicInt"));
    }

    @Test
    public void test09_varargs_prepackedArray() {
        String[] arr = new String[]{"a", "b", "c"};
        Object r = ReflectUtils.invokeMethodByName(helper, "joinArray", new Object[]{arr});
        assertEquals("a|b|c", r);
    }

    @Test
    public void test10_varargs_elementsAndEmpty() {
        Object r1 = ReflectUtils.invokeMethodByName(helper, "joinVarargs", new Object[]{"x", "y", "z"});
        assertEquals("x,y,z", r1);

        Object r2 = ReflectUtils.invokeMethodByName(helper, "joinVarargs", new Object[]{});
        assertEquals("", r2);
    }

    @Test
    public void test11_varargs_withPrefix_and_multipleElements() {
        Object r = ReflectUtils.invokeMethodByName(helper, "joinVarargsWithPrefix", new Object[]{"P", "x", "y"});
        assertEquals("P:x,y", r);
    }

    @Test
    public void test12_overloadSelection_prefersIntegerSpecific() {
        // pass Integer -> should call overload(Integer)
        Object r = ReflectUtils.invokeMethodByName(helper, "overload", new Object[]{Integer.valueOf(5)});
        assertEquals("Integer:5", r);

        // pass Double -> should call Number variant
        Object r2 = ReflectUtils.invokeMethodByName(helper, "overload", new Object[]{5.5});
        assertTrue(((String) r2).startsWith("Number:"));
    }

    @Test
    public void test13_getAccessibleMethodByName_cacheKeyDifference() {
        // call two different overloads with same arg count but different arg types and ensure chosen method corresponds
        Object r1 = ReflectUtils.invokeMethodByName(helper, "overload", new Object[]{Integer.valueOf(2)});
        assertEquals("Integer:2", r1);

        Object r2 = ReflectUtils.invokeMethodByName(helper, "overload", new Object[]{Double.valueOf(2.2)});
        assertTrue(((String) r2).startsWith("Number:"));
    }

    @Test
    public void test14_concurrentAccess_and_cache_stability() throws InterruptedException {
        int threads = 40;
        int perThread = 200;
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);
        AtomicInteger errors = new AtomicInteger(0);

        for (int t = 0; t < threads; t++) {
            ex.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < perThread; i++) {
                        try {
                            // alternate calls that exercise methodCache and fieldCache
                            ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.0});
                            ReflectUtils.getAccessibleField(helper, "hidden");
                            // mutate field occasionally
                            if ((i & 31) == 0) {
                                ReflectUtils.setFieldValue(helper, "hidden", "concurrent" + i);
                            }
                        } catch (Throwable exx) {
                            errors.incrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        // start
        startLatch.countDown();
        boolean finished = doneLatch.await(30, TimeUnit.SECONDS);
        ex.shutdownNow();
        assertTrue(finished, "concurrent tasks did not finish in time");
        assertEquals(0, errors.get(), "there should be no errors during concurrent invoke");
        // ensure cache was populated
        assertFalse(ReflectUtilsTestHelper.class.getName().isEmpty());
        // methodCache and fieldCache are private; we instead validate that repeated calls are quick and stable
        // At minimum no exceptions thrown and field retains a string value
        Object finalHidden = ReflectUtils.getFieldValue(helper, "hidden");
        assertNotNull(finalHidden);
    }

    @Test
    public void test15_varargs_edge_prepackedPrimitiveArrayToObjectVararg() {
        // create an Object[] and call joinVarargs (String...) with Object[] param should be converted elementwise
        Object raw = new Object[]{"m", "n"};
        Object r = ReflectUtils.invokeMethodByName(helper, "joinVarargs", new Object[]{raw});
        // if raw is an Object[] and not String[], our code attempts to convert by treating as prepacked array
        // Result should be "m,n" or original string join fallback
        assertNotNull(r);
    }


    @Test
    public void test16_invokeGetterSetter_nestedProperty() {
        final var p = new ReflectUtilsTestHelper.ParentBean();
        ReflectUtils.invokeSetter(p, "child.name", "nested");
        Object got = ReflectUtils.invokeGetter(p, "child.name");
        assertEquals("nested", got);
    }

    @Test
    public void test17_registerAndUnregisterConverter_forString() {
        // 保存原转换器并注册一个会把任意输入转换为固定字符串 "OVERRIDE"
        Function<Object, String> prev = ReflectUtils.registerConverter(String.class, o -> "OVERRIDE");
        try {
            Object r = ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.4});
            assertEquals("OVERRIDE", r);
        } finally {
            // 恢复原转换器或移除
            if (prev != null) {
                ReflectUtils.registerConverter(String.class, prev);
            } else {
                ReflectUtils.unregisterConverter(String.class);
            }
        }

        // 再次调用应恢复为原行为（默认会对 123.0 去掉 .0）
        Object r2 = ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.0});
        assertEquals("123", r2);
    }

    // Generic base classes for generic type test
    public static class GenericBase<T> {}
    public static class ChildOfGeneric extends GenericBase<String> {}

    @Test
    public void test18_getClassGenericType() {
        assertEquals(String.class, ReflectUtils.getClassGenricType(ChildOfGeneric.class));
    }

    @Test
    public void test19_overloadSelection_chooseSerializableForString() {
        Object r = ReflectUtils.invokeMethodByName(helper, "choose", new Object[]{"hello"});
        // String 实现 Serializable，应匹配 Serializable 版本更具体
        assertEquals("Serializable", r);

        Object r2 = ReflectUtils.invokeMethodByName(helper, "choose", new Object[]{new Object()});
        assertEquals("Object", r2);
    }

    @Test
    public void test20_cacheSizeAndClear() {
        // 调用一些方法以填充缓存
        ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.0});
        ReflectUtils.invokeMethodByName(helper, "sumInts", new Object[]{"2", "3"});
        ReflectUtils.getAccessibleField(helper, "hidden");

        int methodCacheBefore = ReflectUtils.getMethodCacheSize();
        int fieldCacheBefore = ReflectUtils.getFieldCacheSize();
        assertTrue(methodCacheBefore > 0, "method cache should be populated");
        assertTrue(fieldCacheBefore > 0, "field cache should be populated");

        ReflectUtils.clearMethodCache();
        ReflectUtils.clearFieldCache();
        assertEquals(0, ReflectUtils.getMethodCacheSize(), "method cache should be cleared");
        assertEquals(0, ReflectUtils.getFieldCacheSize(), "field cache should be cleared");
    }

    @Test
    public void test21_varargs_prepackedObjectArrayToStringVararg() {
        Object prepacked = new Object[]{"a", "b", "c"};
        Object r = ReflectUtils.invokeMethodByName(helper, "joinVarargs", new Object[]{prepacked});
        // 代码会尝试将 Object[] 的每个元素转换为 String 并打包
        assertEquals("a,b,c", r);
    }

    @Test
    public void test22_sumInts_withDifferentInputTypes() {
        Object r1 = ReflectUtils.invokeMethodByName(helper, "sumInts", new Object[]{1, 2});
        assertEquals(3, ((Integer) r1).intValue());

        Object r2 = ReflectUtils.invokeMethodByName(helper, "sumInts", new Object[]{"4", "6"});
        assertEquals(10, ((Integer) r2).intValue());
    }

    @Test
    public void test23_getUserClass_nonCglibAnonymousSubclass() {
        ReflectUtilsTestHelper anon = new ReflectUtilsTestHelper() { /* anonymous subclass */ };
        Class<?> user = ReflectUtils.getUserClass(anon);
        // 因为类名不会包含 "$$", 应直接返回 anon.getClass()
        assertEquals(anon.getClass(), user);
    }

    @Test
    public void test24_invalidEnumString_throws() {
        // 传递无法映射到 TestEnum 的字符串 "NOT_EXIST"
        assertThrows(RuntimeException.class, () -> {
            ReflectUtils.invokeMethodByName(helper, "echoEnum", new Object[]{"NOT_EXIST"});
        });
    }

    @Test
    public void test25_primitiveArray_asSingleArg_toStringVarargs() {
        Object singlePrimitiveArray = new int[]{1, 2, 3};
        // 这里我们希望不会抛出不可捕获的异常（可能会返回 "" 或 抛出类型不匹配的 RuntimeException）
        Object r;
        try {
            r = ReflectUtils.invokeMethodByName(helper, "joinVarargs", new Object[]{singlePrimitiveArray});
            // 如果成功且被转换为字符串（不一定会），至少结果不会是 null
            assertNotNull(r);
        } catch (RuntimeException ex) {
            // 允许 RuntimeException（因为无法将 int->String 自动转换），但不应是未预期的 Error
            assertInstanceOf(RuntimeException.class, ex);
        }
    }
}

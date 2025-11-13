package com.satoriviacafe.common.utils.reflect;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 额外的类型转换测试：BigDecimal、Character、byte[] 等。
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ReflectUtilsConversionExtraTest {

    private ReflectUtilsTestHelper helper;

    @BeforeEach
    public void setUp() {
        helper = new ReflectUtilsTestHelper();
        ReflectUtils.clearMethodCache();
        ReflectUtils.clearFieldCache();
        ReflectUtils.setTrimTrailingDotZero(true);
    }

    @Test
    public void conv01_bigDecimal_fromStringAndNumber() {
        // We will make a small helper method to accept BigDecimal via reflection
        class Holder {
            public BigDecimal receive(BigDecimal b) {
                return b;
            }
        }
        Holder h = new Holder();
        Object r1 = ReflectUtils.invokeMethodByName(h, "receive", new Object[]{"123.45"});
        assertInstanceOf(BigDecimal.class, r1);
        assertEquals(new BigDecimal("123.45"), r1);

        Object r2 = ReflectUtils.invokeMethodByName(h, "receive", new Object[]{123.0});
        assertInstanceOf(BigDecimal.class, r2);
    }

    @Test
    public void conv02_character_fromStringAndChar() {
        class Holder {
            public Character recv(Character c) { return c; }
        }
        Holder h = new Holder();
        Object r1 = ReflectUtils.invokeMethodByName(h, "recv", new Object[]{"Z"});
        assertEquals('Z', ((Character) r1).charValue());

        Object r2 = ReflectUtils.invokeMethodByName(h, "recv", new Object[]{'Q'});
        assertEquals('Q', ((Character) r2).charValue());
    }

    @Test
    public void conv03_byteArray_fromString_and_number_and_direct() {
        class Holder {
            public byte[] recv(byte[] b) { return b; }
        }
        Holder h = new Holder();
        Object r1 = ReflectUtils.invokeMethodByName(h, "recv", new Object[]{"abc"});
        assertInstanceOf(byte[].class, r1);
        assertArrayEquals("abc".getBytes(), (byte[]) r1);

        Object r2 = ReflectUtils.invokeMethodByName(h, "recv", new Object[]{(byte) 5});
        assertTrue(r2 instanceof byte[]);
        assertArrayEquals(new byte[]{5}, (byte[]) r2);

        byte[] src = new byte[]{1,2,3};
        Object r3 = ReflectUtils.invokeMethodByName(h, "recv", new Object[]{src});
        assertArrayEquals(src, (byte[]) r3);
    }
}

package it.unict.artool.codesamples.fortest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConditionalSampleTest {

    @Test
    void method1Test() {
        Assertions.assertEquals(-1, ConditionalSample.method1(-1));
        Assertions.assertEquals(-1, ConditionalSample.method1(-2));
        Assertions.assertEquals(4, ConditionalSample.method1(2));
        Assertions.assertEquals(20, ConditionalSample.method1(10));
        Assertions.assertEquals(400, ConditionalSample.method1(200));
        Assertions.assertEquals(5000, ConditionalSample.method1(1501));
    }

    @Test
    void method2Test() {
        Assertions.assertEquals(0, ConditionalSample.method2(-1));
        Assertions.assertEquals(51, ConditionalSample.method2(0));
        Assertions.assertEquals(3, ConditionalSample.method2(1));
        Assertions.assertEquals(4, ConditionalSample.method2(2));
        Assertions.assertEquals(0, ConditionalSample.method2(-2));
    }

    @Test
    void method3Test() {
        Assertions.assertEquals(0, ConditionalSample.method3(-1));
        Assertions.assertEquals(3, ConditionalSample.method3(0));
        Assertions.assertEquals(1, ConditionalSample.method3(1));
        Assertions.assertEquals(1, ConditionalSample.method3(2));
        Assertions.assertEquals(1, ConditionalSample.method3(-2));
        Assertions.assertEquals(2, ConditionalSample.method3(3));
        Assertions.assertEquals(1, ConditionalSample.method3(4));
    }
}



package it.unict.artool.codesamples.fortest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SingleMethodSampleTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void mainTest(){
        String[] args = null;
        SingleMethodSample.main(args);
        Assertions.assertEquals(
                "TESTSTRING\r\n" +
                "TEST_STRINGTESTSTRING\r\n" +
                "teststring\r\n" +
                "test_stringteststring",
                outputStreamCaptor.toString()
                .trim());
    }

}



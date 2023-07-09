package it.unict.artool.codesamples.fortest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ListSampleTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void mainTest() {
        String[] args = null;
        ListSample.main(args);
        Assertions.assertEquals(
                "TEST 4",
                outputStreamCaptor.toString()
                        .trim());
    }
}

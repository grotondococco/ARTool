package it.unict.artool.codesamples;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CycleSample {

    public void methodWithCycle() {
        List<Integer> integerList = List.of(1, 2, 3);
        for (Integer i : integerList) {
            log.info("Number in list: {}", i);
        }
    }

    public void methodWithCycle2() {
        List<Integer> integerList = List.of(1, 2, 3);
        for (Integer i : integerList) {
            if (i % 2 == 0) {
                log.info("The number {} is Odd", i);
            } else {
                log.info("The number {} is Even", i);
            }
        }
    }

}

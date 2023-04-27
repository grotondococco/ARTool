package it.unict.JavaCodeSamples;

import java.util.List;

public class CycleSample {
    public void cycle() {
        List<Integer> integerList = List.of(1, 2, 3);
        for (Integer i : integerList) {
            System.out.println(i);
        }
    }
    public void cycle2() {
        List<Integer> integerList = List.of(1, 2, 3);
        for (Integer i : integerList) {
            System.out.println(i);
        }
    }
}

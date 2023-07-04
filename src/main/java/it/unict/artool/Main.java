package it.unict.artool;

import it.unict.artool.core.Artool;
import it.unict.artool.enums.AlgorithmRecognition;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("ARTool Demo Start");
        Set<AlgorithmRecognition> algorithmRecognitionSet = Set.of(AlgorithmRecognition.values());
        Artool.generateVariant(algorithmRecognitionSet);
    }

}
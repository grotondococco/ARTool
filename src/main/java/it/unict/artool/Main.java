package it.unict.artool;

import it.unict.artool.core.Artool;
import it.unict.artool.enums.AlgorithmRecognition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.TreeSet;

@Slf4j
public class Main {

    public static final String IN_PATH = "./IO/in/";
    public static final String OUT_PATH = "./IO/out/";

    public static void main(String[] args) {
        log.info("ARTool Demo Start");
        TreeSet<AlgorithmRecognition> algorithmRecognitionSet = new TreeSet<>(List.of(AlgorithmRecognition.values()));
        Artool.generateVariant(algorithmRecognitionSet,IN_PATH,OUT_PATH);
    }

}
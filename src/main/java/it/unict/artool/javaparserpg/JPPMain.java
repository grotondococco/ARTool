package it.unict.artool.javaparserpg;

import it.unict.artool.core.Artool;
import it.unict.artool.enums.AlgorithmRecognition;
import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.TreeSet;

@Slf4j
public class JPPMain {

    public static final String SAMPLES_IN_PATH = "src/main/java/it/unict/artool/codesamples/";
    public static final String SAMPLES_OUT_PATH = "src/main/java/it/unict/artool/codesamples/out/";
    public static final String SAMPLES_TEST_PATH = "src/main/java/it/unict/artool/codesamples/fortest/";

    public static void main(String[] args) {
        LoggerUtil.logMethodStart(log);
        Artool.generateVariant(new TreeSet<>(List.of(AlgorithmRecognition.CONDITIONAL)),SAMPLES_TEST_PATH,SAMPLES_TEST_PATH);
        LoggerUtil.logMethodEnd(log);
    }

}

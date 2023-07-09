package it.unict.artool;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
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
        TypeSolver typeSolver = new ReflectionTypeSolver();
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getParserConfiguration().setSymbolResolver(symbolSolver);
        TreeSet<AlgorithmRecognition> algorithmRecognitionSet = new TreeSet<>(List.of(AlgorithmRecognition.values()));
        Artool.generateVariant(algorithmRecognitionSet, IN_PATH, OUT_PATH);
    }

}
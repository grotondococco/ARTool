package it.unict.artool.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.IfStmt;
import it.unict.artool.enums.AlgorithmRecognition;
import it.unict.artool.enums.Errors;
import it.unict.artool.util.CodeGenerationUtil;
import it.unict.artool.util.Constants;
import it.unict.artool.util.JPUtil;
import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class Artool {

    public static void generateVariant(Set<AlgorithmRecognition> algorithmRecognitionSet) {
        Path inputPath = Path.of(Constants.INPUT_DIR);
        log.info("Reading standard input directory: {}", inputPath.normalize().toAbsolutePath());
        List<CompilationUnit> compilationUnitList = JPUtil.getCompilationUnitList(inputPath);
        log.info("Found: {} Java files.", compilationUnitList.size());
        for (CompilationUnit compilationUnit : compilationUnitList) {
            compilationUnit.addOrphanComment(new LineComment("Variant file generating using:"));
            for (AlgorithmRecognition algorithmRecognition : algorithmRecognitionSet) {
                generateAlgorithmVariant(compilationUnit, algorithmRecognition);
            }
            CodeGenerationUtil.outputVariant(compilationUnit);
        }
    }

    private static void generateAlgorithmVariant(CompilationUnit compilationUnit, AlgorithmRecognition algorithmRecognition) {
        switch (algorithmRecognition) {
            case CONDITIONAL -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.CONDITIONAL.getDescription()));
                generateAlgorithmVariantLookingForMultipleIf(compilationUnit);
            }
            case ARRAY -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.ARRAY.getDescription()));
                generateAlgorithmVariantLookingForArray(compilationUnit);
            }
            case LIST -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.LIST.getDescription()));
                generateAlgorithmVariantLookingForList(compilationUnit);
            }
            case SET -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.SET.getDescription()));
                generateAlgorithmVariantLookingForSet(compilationUnit);
            }
            case SINGLEMETHODCALL -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.SINGLEMETHODCALL.getDescription()));
                generateAlgorithmVariantLookingForSigleMethodCall(compilationUnit);
            }
            case BESTCOLLECTION -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.BESTCOLLECTION.getDescription()));
                generateAlgorithmVariantLookingForBestCollection(compilationUnit);
            }
            default -> {
                log.error(Errors.ALGORITHM_NOT_SUPPORTED.getDescription());
                throw new RuntimeException(Errors.ALGORITHM_NOT_SUPPORTED.getDescription() + ": " + algorithmRecognition);
            }
        }
    }

    private static void generateAlgorithmVariantLookingForMultipleIf(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        compilationUnit.addOrphanComment(new LineComment("Artool.generateAlgorithmVariantLookingForMultipleIf Usage"));
        ConditionalModifier conditionalModifier = new ConditionalModifier();
        conditionalModifier.visit(compilationUnit,null);
    }

    private static void generateAlgorithmVariantLookingForArray(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        compilationUnit.addOrphanComment(new LineComment("Artool.generateAlgorithmVariantLookingForArray Usage"));
    }

    private static void generateAlgorithmVariantLookingForList(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        compilationUnit.addOrphanComment(new LineComment("Artool.generateAlgorithmVariantLookingForList Usage"));
    }

    private static void generateAlgorithmVariantLookingForSet(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        compilationUnit.addOrphanComment(new LineComment("Artool.generateAlgorithmVariantLookingForSet Usage"));
    }

    private static void generateAlgorithmVariantLookingForSigleMethodCall(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        compilationUnit.addOrphanComment(new LineComment("Artool.generateAlgorithmVariantLookingForSigleMethodCall Usage"));
    }

    private static void generateAlgorithmVariantLookingForBestCollection(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        compilationUnit.addOrphanComment(new LineComment("Artool.generateAlgorithmVariantLookingForBestCollection Usage"));
    }

}

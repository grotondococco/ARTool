package it.unict.artool.core;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.LineComment;
import it.unict.artool.enums.AlgorithmRecognition;
import it.unict.artool.enums.Errors;
import it.unict.artool.util.JPUtil;
import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Slf4j
public class Artool {

    public static void generateVariant(Set<AlgorithmRecognition> algorithmRecognitionSet, String INPUT_DIR, String OUTPUT_DIR) {
        Path inputPath = Path.of(INPUT_DIR);
        log.info("Reading standard input directory: {}", inputPath.normalize().toAbsolutePath());
        List<CompilationUnit> compilationUnitList = JPUtil.getCompilationUnitList(inputPath);
        log.info("Found: {} Java files.", compilationUnitList.size());
        for (CompilationUnit compilationUnit : compilationUnitList) {
            compilationUnit.addOrphanComment(new LineComment("ARTool: Variant file generated using the following list of Algorithm Recognition:"));
            for (AlgorithmRecognition algorithmRecognition : algorithmRecognitionSet) {
                generateAlgorithmVariant(compilationUnit, algorithmRecognition);
            }
            JPUtil.outputVariant(compilationUnit,OUTPUT_DIR);
        }
    }

    private static void generateAlgorithmVariant(CompilationUnit compilationUnit, AlgorithmRecognition algorithmRecognition) {
        switch (algorithmRecognition) {
            case CONDITIONAL -> {
                compilationUnit.addOrphanComment(new LineComment(AlgorithmRecognition.CONDITIONAL.getDescription()));
                generateAlgorithmVariantLookingForMultipleIf(compilationUnit);
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
            default -> {
                log.error(Errors.ALGORITHM_NOT_SUPPORTED.getDescription());
                throw new RuntimeException(Errors.ALGORITHM_NOT_SUPPORTED.getDescription() + ": " + algorithmRecognition);
            }
        }
    }

    private static void generateAlgorithmVariantLookingForMultipleIf(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        new ConditionalModifier().visit(compilationUnit, null);
    }

    private static void generateAlgorithmVariantLookingForArray(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
    }

    private static void generateAlgorithmVariantLookingForList(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
    }

    private static void generateAlgorithmVariantLookingForSet(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        SetModifier setModifier = new SetModifier();
        setModifier.visit(compilationUnit,null);
        setModifier.logEnd();
    }

    private static void generateAlgorithmVariantLookingForSigleMethodCall(CompilationUnit compilationUnit) {
        LoggerUtil.logMethodStart(log);
        new SingleMethodModifier().visit(compilationUnit,null);
    }


}

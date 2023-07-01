package it.unict.artool.core;

import com.github.javaparser.ast.CompilationUnit;
import it.unict.artool.util.CodeGenerationUtil;
import it.unict.artool.util.Constants;
import it.unict.artool.util.JPUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
@Slf4j
public class Artool {

    public static void generateVariant(){
        Path inputPath = Path.of(Constants.INPUT_DIR);
        log.info("Reading standard input directory: {}",inputPath.normalize().toAbsolutePath());
        List<CompilationUnit> compilationUnitList = JPUtil.getCompilationUnitList(inputPath);
        log.info("Found: {} Java files.",compilationUnitList.size());
        for (CompilationUnit compilationUnit : compilationUnitList) {
            // n test n generation of variants
            CodeGenerationUtil.generateVariant(compilationUnit);
        }
    }

}

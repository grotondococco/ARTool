package it.unict.artool.util;

import com.github.javaparser.ast.CompilationUnit;
import it.unict.artool.enums.Errors;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CodeGenerationUtil {

    public static void main(String[] args) {
        generateAllVariant();
    }

    /**
     * Streams out the CompilationUnit into a java file and saves it in the standard SAVE_DIR defined in Constants Interface.
     *
     * @param cu the given Compilatioun unit representing the parsed java file
     */
    public static void generateVariant(CompilationUnit cu) {
        File newFile = null;
        try {
            Optional<String> primaryTypeName = cu.getPrimaryTypeName();
            String fileName = primaryTypeName.map((name) -> name + ".java").orElse("test.java");
            newFile = new File(Constants.SAVE_DIR + fileName);
            if (newFile.createNewFile()) {
                log.info("Variant file {} created: @ {}", newFile.getName(), Constants.SAVE_DIR);
            }
        } catch (IOException e) {
            log.error("Cannot create file.");
        }
        try (FileWriter fileWriter = new FileWriter(newFile)) {
            fileWriter.write(cu.toString());
            log.info("Variant file generated @ {}{}", Constants.SAVE_DIR, newFile.getName());
        } catch (IOException e) {
            log.error(Errors.GENERIC.getDescrption());
        }
    }

    /**
     * Streams out the CompilationUnit into a java file and saves it in the given file path.
     *
     * @param cu       the given CompilationUnit representing the parsed java file
     * @param filePath the given String as relative path to the variant
     */
    public static void generateVariant(CompilationUnit cu, String filePath) {
        File newFile = null;
        try {
            Optional<String> primaryTypeName = cu.getPrimaryTypeName();
            String fileName = primaryTypeName.map((name) -> name + ".java").orElse("test.java");
            newFile = new File(filePath + fileName);
            if (newFile.createNewFile()) {
                log.info("Variant file {} created: @ {}", newFile.getName(), Constants.SAVE_DIR);
            }
        } catch (IOException e) {
            log.error("Cannot create file.");
        }
        try (FileWriter fileWriter = new FileWriter(newFile)) {
            fileWriter.write(cu.toString());
            log.info("Variant file generated @ {}{}", filePath, newFile.getName());
        } catch (IOException e) {
            log.error(Errors.GENERIC.getDescrption());
        }
    }

    public static void generateAllVariant() {
        List<CompilationUnit> compilationUnitList = JPUtil.getCompilationUnitList(Constants.INPUT_DIR);
        for (CompilationUnit compilationUnit : compilationUnitList) {
            CodeGenerationUtil.generateVariant(compilationUnit);
        }
    }

}

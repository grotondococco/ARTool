package it.unict.artool.util;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import it.unict.artool.enums.Errors;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JPUtil {


    public static Optional<CompilationUnit> getCompilationUnitFromFile(String filePath) {
        try {
            return Optional.of(StaticJavaParser.parse(new File(filePath)));
        } catch (FileNotFoundException e) {
            log.error(Errors.FILE_NOT_FOUND.getDescription());
        }
        return Optional.empty();
    }

    /**
     * Extracts from a directory a list of CompilationUnit representing parsed java files
     *
     * @param inputPath the given path of directory containing java files
     */
    public static List<CompilationUnit> getCompilationUnitList(Path inputPath) {
        List<CompilationUnit> compilationUnitList = new ArrayList<>();
        if (Files.isDirectory(inputPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(inputPath)) {
                for (Path path : directoryStream) {
                    Optional<String> pathString = FileUtil.getExtensionByStringHandling(path.toString());
                    if (pathString.isPresent() && pathString.get().equalsIgnoreCase("java")) {
                        Optional<CompilationUnit> compilationUnit = JPUtil.getCompilationUnitFromFile(path.toString());
                        compilationUnit.ifPresent(compilationUnitList::add);
                    }
                }
            } catch (IOException e) {
                log.error(Errors.FILE_NOT_FOUND.getDescription());
            }
        }
        return compilationUnitList;
    }

    /**
     * Streams out the CompilationUnit into a java file and saves it in the standard SAVE_DIR defined in Constants Interface.
     *
     * @param cu the given Compilatioun unit representing the parsed java file
     */
    public static void outputVariant(CompilationUnit cu, String OUTPUT_DIR) {
        File newFile = null;
        try {
            Optional<String> primaryTypeName = cu.getPrimaryTypeName();
            String fileName = primaryTypeName.map((name) -> name + ".java").orElse("test.java");
            newFile = new File(OUTPUT_DIR + fileName);
            if (newFile.createNewFile()) {
                log.info("Variant file {} created.", Path.of(newFile.getAbsolutePath()).normalize());
            }
        } catch (IOException e) {
            log.error("Cannot create file.");
        }
        try (FileWriter fileWriter = new FileWriter(newFile)) {
            fileWriter.write(cu.toString());
            log.info("Variant file generated {}.", Path.of(newFile.getAbsolutePath()).normalize());
        } catch (IOException e) {
            log.error(Errors.GENERIC.getDescription());
        }
    }

}

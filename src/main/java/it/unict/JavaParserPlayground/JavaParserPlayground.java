package it.unict.JavaParserPlayground;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;
import it.unict.Util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
public class JavaParserPlayground {

    public void printMethods(String filePath) {
        LoggerUtil.logMethodStart(log);
        try {
            CompilationUnit cu = StaticJavaParser.parse(new File(filePath));
            MethodNamePrinter methodNamePrinter = new MethodNamePrinter();
            printMethodList(methodNamePrinter.getMethodList(cu));
        } catch (FileNotFoundException e) {
            log.error("Input file not found.");
        }
        LoggerUtil.logMethodEnd(log);
    }

    private void printMethodList(List<String> methodList){
        LoggerUtil.logMethodStart(log);
        if (methodList.isEmpty()){
            log.info("No methods found");
            return;
        }
        log.info("Methods found: " + methodList);
        LoggerUtil.logMethodEnd(log);
    }
}
package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import it.unict.artool.enums.Errors;
import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
            log.error(Errors.FILE_NOT_FOUND.getDescrption());
        }
        LoggerUtil.logMethodEnd(log);
    }

    private void printMethodList(List<String> methodList) {
        LoggerUtil.logMethodStart(log);
        if (methodList.isEmpty()) {
            log.info("No methods found");
            return;
        }
        log.info("Methods found: " + methodList);
        LoggerUtil.logMethodEnd(log);
    }

    private List<String> getMethodListFromVisitor(String filePath) {
        LoggerUtil.logMethodStart(log);
        List<String> methodNames = new ArrayList<>();
        VoidVisitor<List<String>> methodNameCollector = new MethodNameCollector();
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (FileNotFoundException e) {
            log.error(Errors.FILE_NOT_FOUND.getDescrption());
        }
        methodNameCollector.visit(cu, methodNames);
        LoggerUtil.logMethodEnd(log);
        return methodNames;
    }

    public void printMethodListUsingVisitor(String filePath) {
        LoggerUtil.logMethodStart(log);
        List<String> methodNames = getMethodListFromVisitor(filePath);
        log.info("Methods found: {}", methodNames);
        LoggerUtil.logMethodEnd(log);
    }

    public void printIntegerLiteralWithModifier(String filePath) {
        LoggerUtil.logMethodStart(log);
        ModifierVisitor<?> numericLiteralVisitor = new IntegerLiteralModifier();
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (FileNotFoundException e) {
            log.error(Errors.FILE_NOT_FOUND.getDescrption());
        }
        numericLiteralVisitor.visit(cu, null);
        LoggerUtil.logMethodEnd(log);
    }

    public void locateLoops(String filePath) {
        LoggerUtil.logMethodStart(log);
        VoidVisitorAdapter<Void> loopLocator = new LoopLocator();
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (FileNotFoundException e) {
            log.error(Errors.FILE_NOT_FOUND.getDescrption());
        }
        loopLocator.visit(cu, null);
        LoggerUtil.logMethodEnd(log);
    }

    public void printComments(String filePath) {
        CommentReporter commentReporter = new CommentReporter();
        List<CommentReportEntry> commentsList = commentReporter.getAllComments(filePath);
        if (!commentsList.isEmpty()) {
            commentsList.forEach(c -> log.info(c.toString()));
        } else {
            log.info("No comments found");
        }
    }

}

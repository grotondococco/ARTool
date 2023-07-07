package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import it.unict.artool.util.CodeGenerationUtil;
import it.unict.artool.util.JPUtil;
import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JavaParserPlayground {

    public void printMethods(String filePath) {
        LoggerUtil.logMethodStart(log);
        Optional<CompilationUnit> cu = JPUtil.getCompilationUnitFromFile(filePath);
        cu.ifPresent(compilationUnit -> printMethodList((MethodNamePrinter.getMethodList(compilationUnit))));
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
        Optional<CompilationUnit> cu = JPUtil.getCompilationUnitFromFile(filePath);
        cu.ifPresent(compilationUnit -> methodNameCollector.visit(cu.get(), methodNames));
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
        Optional<CompilationUnit> cu = JPUtil.getCompilationUnitFromFile(filePath);
        cu.ifPresent(compilationUnit -> numericLiteralVisitor.visit(cu.get(), null));
        CodeGenerationUtil.outputVariant(cu.get(), JPPMain.OUT_PATH);
        LoggerUtil.logMethodEnd(log);
    }

    public void locateLoops(String filePath) {
        LoggerUtil.logMethodStart(log);
        VoidVisitorAdapter<Void> loopLocator = new LoopLocator();
        Optional<CompilationUnit> cu = JPUtil.getCompilationUnitFromFile(filePath);
        cu.ifPresent(compilationUnit -> loopLocator.visit(cu.get(), null));
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

package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import it.unict.artool.enums.Errors;
import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<BlockStmt, List<IfStmt>> getIfStatementMap(String filePath) {
        LoggerUtil.logMethodStart(log);
        Map<BlockStmt, List<IfStmt>> blockStmtListMap = new HashMap<>();
        ConditionalModifier conditionalModifier = new ConditionalModifier();
        conditionalModifier.initMap();
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (FileNotFoundException e) {
            log.error(Errors.FILE_NOT_FOUND.getDescrption());
        }
        conditionalModifier.visit(cu, blockStmtListMap);
        LoggerUtil.logMethodEnd(log);
        return blockStmtListMap;
    }

    public void printIfStatementsinSameBlock(String filePath) {
        LoggerUtil.logMethodStart(log);
        Map<BlockStmt, List<IfStmt>> blockStmtListMap = getIfStatementMap(filePath);
        for (BlockStmt b : blockStmtListMap.keySet()) {
            log.info("Found Block Statement containing \"if conditions\" @ Line[{}]:\n {}", b.getRange().get().begin.line, blockStmtListMap.get(b));
        }
        LoggerUtil.logMethodEnd(log);
    }

    public void printMultipleIfStatements(String filePath) {
        LoggerUtil.logMethodStart(log);
        Map<BlockStmt, List<IfStmt>> blockStmtListMap = getIfStatementMap(filePath);
        Map<String, List<String>> multipleIfMap = new HashMap<>();
        for (BlockStmt b : blockStmtListMap.keySet()) {
            List<IfStmt> ifStmtList = blockStmtListMap.get(b);
            for (IfStmt ifStmt : ifStmtList) {
                String conditionLeft = ifStmt.getCondition().asBinaryExpr().getLeft().toString();
                if (!multipleIfMap.containsKey(conditionLeft)) {
                    multipleIfMap.put(conditionLeft, new ArrayList<>());
                }
                multipleIfMap.get(conditionLeft).add(String.valueOf(ifStmt.getRange().get().begin.line));
            }
        }
        for (String b : multipleIfMap.keySet()) {
            if (multipleIfMap.get(b).size() > 1) {
                log.info("For the variable \"{}\" - found usage in multiple if statement @ lines: {}", b, multipleIfMap.get(b));
            }
        }
        LoggerUtil.logMethodEnd(log);
    }

}

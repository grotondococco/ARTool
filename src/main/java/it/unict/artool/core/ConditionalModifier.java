package it.unict.artool.core;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import it.unict.artool.enums.Errors;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConditionalModifier extends ModifierVisitor<Void> {

    @Override
    public Visitable visit(BlockStmt n, Void arg) {
        super.visit(n, arg);
        analyze(n);
        return n;
    }

    private void analyze(BlockStmt n) {
        List<IfStmt> ifStmtList = n.findAll(IfStmt.class);
        if (!ifStmtList.isEmpty()) {
            List<IfStmt> ifStmtToConvert = getIfStmtHavingSameLeftExpression(ifStmtList);
            if (!ifStmtToConvert.isEmpty()) {
                log.info("Found {} If Statements over the same argument to convert. Generating Switch Variant...", ifStmtToConvert.size());
                IfStmt sampleIfStmt = ifStmtToConvert.stream().findAny().orElseThrow(() -> new RuntimeException(Errors.GENERIC.getDescription()));
                NodeList<Statement> nodeList = n.getStatements();
                nodeList.addBefore(generateSwitchVariantFromIfstmtList(ifStmtToConvert), sampleIfStmt);
                nodeList.removeAll(ifStmtToConvert);
            } else {
                log.info("No If Statements over the same argument to convert found.");
            }
        }
    }

    private Expression getLeftExpressionFromIfStmtAsBinaryExpr(IfStmt ifStmt) {
        return ifStmt.getCondition().asBinaryExpr().getLeft();
    }

    private BinaryExpr.Operator getOperatorFromIfStmtAsBinaryExpr(IfStmt ifStmt) {
        return ifStmt.getCondition().asBinaryExpr().getOperator();
    }

    private Expression getRightExpressionFromIfStmtAsBinaryExpr(IfStmt ifStmt) {
        return ifStmt.getCondition().asBinaryExpr().getRight();
    }

    private List<IfStmt> getIfStmtHavingSameLeftExpression(List<IfStmt> givenList) {
        log.info("Checking for multiple If Statements used over the same argument...");
        List<IfStmt> resultList = new ArrayList<>();
        for (IfStmt ifStmt : givenList) {
            for (IfStmt ifStmtInternal : givenList) {
                if (!ifStmt.equals(ifStmtInternal) &&
                        getLeftExpressionFromIfStmtAsBinaryExpr(ifStmt).equals(getLeftExpressionFromIfStmtAsBinaryExpr(ifStmtInternal)) &&
                        getOperatorFromIfStmtAsBinaryExpr(ifStmt).equals(getOperatorFromIfStmtAsBinaryExpr(ifStmtInternal))
                ) {
                    if (!resultList.contains(ifStmt)) {
                        resultList.add(ifStmt);
                    }
                    if (!resultList.contains(ifStmtInternal)) {
                        resultList.add(ifStmtInternal);
                    }
                }
            }
        }
        return resultList;
    }

    private SwitchStmt generateSwitchVariantFromIfstmtList(List<IfStmt> ifStmtList) {
        IfStmt sampleIfStmt = ifStmtList.stream().findAny().orElseThrow(() -> new RuntimeException(Errors.GENERIC.getDescription()));
        SwitchStmt switchStmt = new SwitchStmt();
        switchStmt.setComment(new LineComment("Multiple If statements on same Left Expression converted into Switch Statement."));
        switchStmt.setSelector(getLeftExpressionFromIfStmtAsBinaryExpr(sampleIfStmt));
        NodeList<SwitchEntry> switchEntryNodeList = new NodeList<>();
        for (IfStmt ifStmt : ifStmtList) {
            SwitchEntry switchEntry = new SwitchEntry();
            switchEntry.setLabels(NodeList.nodeList(getRightExpressionFromIfStmtAsBinaryExpr(ifStmt)));
            for (Node node : ifStmt.getThenStmt().getChildNodes()) {
                switchEntry.addStatement(node.toString());
            }
            if (switchEntry.getStatements().stream().filter(statement -> statement instanceof ReturnStmt).findAny().isEmpty()) {
                switchEntry.addStatement(new BreakStmt());
            }
            switchEntryNodeList.add(switchEntry);
        }

        //default
        switchEntryNodeList.add(new SwitchEntry().addStatement(new BreakStmt()));

        switchStmt.setEntries(switchEntryNodeList);
        return switchStmt;
    }

}

package it.unict.artool.core;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import it.unict.artool.enums.Errors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            Set<IfStmt> ifStmtToConvert = getIfStmtHavingSameLeftExpression(ifStmtList);
            if (!ifStmtToConvert.isEmpty()) {
                IfStmt sampleIfStmt = ifStmtToConvert.stream().findAny().orElseThrow(() -> new RuntimeException(Errors.GENERIC.getDescription()));
                NodeList<Statement> nodeList = n.getStatements();
                nodeList.addBefore(generateSwitchVariantFromIfstmtSet(ifStmtToConvert), sampleIfStmt);
                nodeList.removeAll(ifStmtToConvert);
            }
        }
    }

    private Expression getLeftExpressionFromIfStmtAsBinaryExpr(IfStmt ifStmt) {
        return ifStmt.getCondition().asBinaryExpr().getLeft();
    }

    private Expression getRightExpressionFromIfStmtAsBinaryExpr(IfStmt ifStmt) {
        return ifStmt.getCondition().asBinaryExpr().getRight();
    }

    private Set<IfStmt> getIfStmtHavingSameLeftExpression(List<IfStmt> givenList) {
        Set<IfStmt> resultSet = new HashSet<>();
        for (IfStmt ifStmt : givenList) {
            for (IfStmt ifStmtInternal : givenList) {
                if (!ifStmt.equals(ifStmtInternal) &&
                        getLeftExpressionFromIfStmtAsBinaryExpr(ifStmt).equals(getLeftExpressionFromIfStmtAsBinaryExpr(ifStmtInternal))
                ) {
                    resultSet.add(ifStmt);
                    resultSet.add(ifStmtInternal);
                }
            }
        }
        return resultSet;
    }

    private SwitchStmt generateSwitchVariantFromIfstmtSet(Set<IfStmt> ifStmtSet) {
        IfStmt sampleIfStmt = ifStmtSet.stream().findAny().orElseThrow(() -> new RuntimeException(Errors.GENERIC.getDescription()));
        SwitchStmt switchStmt = new SwitchStmt();
        switchStmt.setComment(new LineComment("Multiple If statements on same Left Expression converted into Switch Statement."));
        switchStmt.setSelector(getLeftExpressionFromIfStmtAsBinaryExpr(sampleIfStmt));
        NodeList<SwitchEntry> switchEntryNodeList = new NodeList<>();
        for (IfStmt ifStmt : ifStmtSet) {
            SwitchEntry switchEntry = new SwitchEntry();
            switchEntry.setLabels(NodeList.nodeList(getRightExpressionFromIfStmtAsBinaryExpr(ifStmt)));
            //switchEntry.addStatement(ifStmt.getThenStmt());
            for (Node node : ifStmt.getThenStmt().getChildNodes()){
                switchEntry.addStatement(node.toString());
            }
            switchEntry.addStatement(new BreakStmt());
            switchEntryNodeList.add(switchEntry);
        }

        //default
        switchEntryNodeList.add(new SwitchEntry().addStatement(new BreakStmt()));

        switchStmt.setEntries(switchEntryNodeList);
        return switchStmt;
    }

}

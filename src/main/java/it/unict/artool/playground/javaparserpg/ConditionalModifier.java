package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

import java.util.Map;
import java.util.Optional;


public class ConditionalModifier extends ModifierVisitor<Map<String, String>> {

    @Override
    public Visitable visit(IfStmt n, Map<String, String> arg) {
        String mapKey = n.getCondition().asBinaryExpr().getLeft().toString();
        arg.put(mapKey, n.getCondition().getRange().map(range -> range.begin.line).orElse(-1).toString());
        String otherUseLine = checkMultipleIf(n);
        if (!otherUseLine.equals("-1")) {
            String existingLines = arg.get(mapKey);
            arg.put(mapKey, existingLines + ", " + otherUseLine);
        }
        return super.visit(n, arg);
    }

    private String checkMultipleIf(IfStmt n) {
        Expression leftExpression = n.getCondition().asBinaryExpr().getLeft();
        Optional<Node> parent = n.getParentNode();
        if (parent.isEmpty()) {
            return "-1";
        }
        if (parent.get() instanceof BlockStmt) {
            NodeList<Statement> siblings = ((BlockStmt) parent.get()).getStatements();
            for (Statement statement : siblings) {
                if (statement instanceof IfStmt && !(statement).equals(n)) {
                    if (((IfStmt) statement).getCondition().asBinaryExpr().getLeft().equals(leftExpression)) {
                        return statement.getRange().map(range -> range.begin.line).orElse(-1).toString();
                    }
                }
            }
        }
        return "-1";
    }

}

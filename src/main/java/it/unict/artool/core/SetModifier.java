package it.unict.artool.core;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@Slf4j
public class SetModifier extends ModifierVisitor<Void> {

    private int convertCount;

    public SetModifier() {
        super();
        convertCount = 0;
    }

    private int getConvertCount() {
        return convertCount;
    }

    @Override
    public Visitable visit(VariableDeclarationExpr n, Void arg) {
        super.visit(n, arg);
        findAndConvertTreeSet(n);
        return n;
    }

    private void findAndConvertTreeSet(VariableDeclarationExpr n) {
        NodeList<VariableDeclarator> variableDeclaratorNodeList = n.getVariables();
        for (VariableDeclarator variableDeclarator : variableDeclaratorNodeList) {
            Type type = variableDeclarator.getType();
            if (type.isClassOrInterfaceType()) {
                SimpleName simpleName = ((ClassOrInterfaceType) type).getName();
                if (simpleName.asString().equals("TreeSet")) {
                    insertComment(n);
                    ((ClassOrInterfaceType) type).setName("HashSet");
                    Optional<Expression> variableInitializer = variableDeclarator.getInitializer();
                    Optional<ObjectCreationExpr> objectCreationExpr = variableInitializer.map(Expression::asObjectCreationExpr);
                    objectCreationExpr.ifPresent(creationExpr -> creationExpr.getType().setName("HashSet"));
                    convertCount++;
                }
            }
        }
    }

    private void insertComment(VariableDeclarationExpr n) {
        n.setComment(new LineComment("ARTool: TreeSet type converted into HashSet type."));
    }

    public void logEnd() {
        log.info("Found and converted {} TreeSet into HashSet.", this.getConvertCount());
    }

}

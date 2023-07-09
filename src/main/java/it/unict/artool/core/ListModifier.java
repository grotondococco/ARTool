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
public class ListModifier extends ModifierVisitor<Void> {

    private int convertCount;

    public ListModifier() {
        super();
        convertCount = 0;
    }

    private int getConvertCount() {
        return convertCount;
    }

    @Override
    public Visitable visit(VariableDeclarationExpr n, Void arg) {
        super.visit(n, arg);
        findAndConvertLinkedList(n);
        return n;
    }

    private void findAndConvertLinkedList(VariableDeclarationExpr n) {
        NodeList<VariableDeclarator> variableDeclaratorNodeList = n.getVariables();
        for (VariableDeclarator variableDeclarator : variableDeclaratorNodeList) {
            Type type = variableDeclarator.getType();
            if (type.isClassOrInterfaceType()) {
                SimpleName simpleName = ((ClassOrInterfaceType) type).getName();
                if (simpleName.asString().equals("LinkedList")) {
                    insertComment(n);
                    ((ClassOrInterfaceType) type).setName("ArrayList");
                    Optional<Expression> variableInitializer = variableDeclarator.getInitializer();
                    Optional<ObjectCreationExpr> objectCreationExpr = variableInitializer.map(Expression::asObjectCreationExpr);
                    objectCreationExpr.ifPresent(creationExpr -> creationExpr.getType().setName("ArrayList"));
                    convertCount++;
                }
            }
        }
    }

    private void insertComment(VariableDeclarationExpr n) {
        n.setComment(new LineComment("ARTool: LinkedList type converted into ArrayList type."));
    }

    public void logEnd() {
        log.info("Found and converted {} LinkedList into ArrayList.", this.getConvertCount());
    }

}

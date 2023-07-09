package it.unict.artool.core;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class SingleMethodModifier extends ModifierVisitor<Void> {

    private static int numberOfVariables = 0;

    @Override
    public Visitable visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        analyze(n);
        return n;
    }

    private void analyze(ClassOrInterfaceDeclaration n) {
        log.info("Checking for Methods called once...");
        List<MethodDeclaration> methodDeclarationList = new ArrayList<>(n.getMethods());
        Optional<MethodDeclaration> mainMethod = methodDeclarationList.stream()
                .filter(methodDeclaration -> methodDeclaration.getNameAsString().equals("main"))
                .findFirst();
        mainMethod.ifPresent(methodDeclarationList::remove);
        List<MethodCallExpr> methodCallExprList = n.findAll(MethodCallExpr.class);
        Map<MethodDeclaration, List<MethodCallExpr>> methodDeclarationMethodCallExprMap = new HashMap<>();
        for (MethodDeclaration methodDeclaration : methodDeclarationList) {
            methodDeclarationMethodCallExprMap.put(methodDeclaration, new ArrayList<>());
            String methodSignature = methodDeclaration.resolve().getQualifiedSignature();
            for (MethodCallExpr methodCallExpr : methodCallExprList) {
                if (methodCallExpr.resolve().getQualifiedSignature().equals(methodSignature)) {
                    methodDeclarationMethodCallExprMap.get(methodDeclaration).add(methodCallExpr);
                }
            }
        }
        List<MethodDeclaration> declarationListToDelete = new ArrayList<>();
        for (MethodDeclaration methodDeclaration : methodDeclarationMethodCallExprMap.keySet()) {
            List<MethodCallExpr> methodCallExprListInternal = methodDeclarationMethodCallExprMap.get(methodDeclaration);
            if (methodCallExprListInternal.size() != 1) {
                declarationListToDelete.add(methodDeclaration);
            }
        }
        for (MethodDeclaration methodDeclaration : declarationListToDelete) {
            methodDeclarationMethodCallExprMap.remove(methodDeclaration);
        }
        log.info("Found {} method called once ", methodDeclarationMethodCallExprMap.size());
        identifyOperationPoints(methodDeclarationMethodCallExprMap);
        for (MethodDeclaration methodDeclaration : methodDeclarationMethodCallExprMap.keySet()) {
            n.remove(methodDeclaration);
        }
    }


    private void identifyOperationPoints(Map<MethodDeclaration, List<MethodCallExpr>> methodDeclarationMethodCallExprMap) {
        for (MethodDeclaration methodDeclaration : methodDeclarationMethodCallExprMap.keySet()) {
            List<MethodCallExpr> methodCallExprList = methodDeclarationMethodCallExprMap.get(methodDeclaration);
            for (MethodCallExpr methodCallExpr : methodCallExprList) {
                transferMethodBodyAtCallPoint(methodDeclaration, methodCallExpr);
            }
        }
    }

    private void transferMethodBodyAtCallPoint(MethodDeclaration methodDeclaration, MethodCallExpr methodCallExpr) {
        Optional<ExpressionStmt> expressionStmt = checkForExpressionStmt(methodCallExpr);
        if (expressionStmt.isEmpty()) {
            log.debug("Method call is not a ExpressionStmt.");
        } else {
            Optional<BlockStmt> blockStmt = expressionStmt.get().getParentNode().map(exp -> (BlockStmt) exp);
            if (blockStmt.isEmpty()) {
                log.debug("The ExpressionStmt is not child of a BlockStmt.");
            } else {
                List<Statement> variantStatementList = getVariantStatementList(methodDeclaration, methodCallExpr);
                NodeList<Statement> blockStmtNodeList = blockStmt.get().getStatements();
                blockStmtNodeList.addAll(getNodeIndexFromNodeList(blockStmtNodeList, expressionStmt.get()), variantStatementList);
                blockStmt.get().remove(expressionStmt.get());
            }
        }
    }

    private Optional<ExpressionStmt> checkForExpressionStmt(MethodCallExpr methodCallExpr) {
        Optional<Node> parentNode = methodCallExpr.getParentNode();
        if (parentNode.isEmpty()) {
            return Optional.empty();
        }
        Optional<Node> grandParentNode = parentNode.get().getParentNode();
        if (grandParentNode.isEmpty()) {
            return Optional.empty();
        }
        Optional<Node> ancestorNode = grandParentNode.get().getParentNode();
        return ancestorNode.map(node -> (ExpressionStmt) node);
    }

    private int getNodeIndexFromNodeList(NodeList<Statement> nodeList, Statement statement) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).equals(statement)) {
                return i;
            }
        }
        return -1;
    }

    private List<Statement> getVariantStatementList(MethodDeclaration methodDeclaration, MethodCallExpr methodCallExpr) {
        List<Statement> statementList = new ArrayList<>();
        Optional<VariableDeclarator> variableDeclaratorFromMethodCallExpr = methodCallExpr.getParentNode().map(node -> (VariableDeclarator) node);
        if (variableDeclaratorFromMethodCallExpr.isEmpty()) {
            return statementList;
        }
        ExpressionStmt variantExpressionStmt = generateVariantExpressionStmtForDeclaration(variableDeclaratorFromMethodCallExpr.get());
        Optional<BlockStmt> blockStmt = generateVariantBlockStmtFromMethodBody(variantExpressionStmt, methodDeclaration, methodCallExpr);
        statementList.add(variantExpressionStmt);
        blockStmt.ifPresent(stmt -> statementList.addAll(stmt.getStatements()));
        return statementList;
    }

    private Optional<BlockStmt> generateVariantBlockStmtFromMethodBody(ExpressionStmt variantExpressionStmt, MethodDeclaration methodDeclaration, MethodCallExpr methodCallExpr) {
        Optional<BlockStmt> methodBody = methodDeclaration.getBody();
        if (methodBody.isEmpty()) {
            log.info("Method body empty: nothing to transfer.");
            return Optional.empty();
        }
        BlockStmt blockStmt = new BlockStmt();
        NodeList<Parameter> parameterNodeList = methodDeclaration.getParameters();
        List<NameExpr> nameExprList = methodBody.get().findAll(NameExpr.class);
        for (int i = 0; i < parameterNodeList.size(); i++) {
            for (NameExpr nameExpr : nameExprList) {
                if (nameExpr.getName().equals(parameterNodeList.get(i).getName())) {
                    nameExpr.setName(methodCallExpr.getArgument(i).asNameExpr().getName());
                }
            }
        }
        List<ReturnStmt> returnStmtList = methodBody.get().findAll(ReturnStmt.class);
        ReturnStmt returnStmt = returnStmtList.get(0);
        VariableDeclarator variableDeclarator = new VariableDeclarator(
                methodDeclaration.getType(), generateNewTmpVariableName(),
                returnStmt.getExpression().orElse(null)
        );
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(new NodeList<>(variableDeclarator));
        ExpressionStmt expressionStmt = new ExpressionStmt(variableDeclarationExpr);
        methodBody.get().remove(returnStmt);
        blockStmt.getStatements().addAll(methodBody.get().getStatements());
        blockStmt.addStatement(expressionStmt);
        SimpleName simpleName = variantExpressionStmt.findAll(SimpleName.class).get(1);
        ExpressionStmt returnExpression = new ExpressionStmt(variantExpressionStmt.getExpression());
        returnExpression.setExpression(simpleName.toString());
        AssignExpr assignExpr = new AssignExpr(
                returnExpression.getExpression(),
                variableDeclarationExpr.getVariables().get(0).getNameAsExpression(),
                AssignExpr.Operator.ASSIGN);
        blockStmt.addStatement(assignExpr);
        return Optional.of(blockStmt);
    }

    private String generateNewTmpVariableName() {
        numberOfVariables++;
        return "tmpVariable_" + numberOfVariables;
    }

    private ExpressionStmt generateVariantExpressionStmtForDeclaration(VariableDeclarator variableDeclarator) {
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr(
                variableDeclarator.getType(),
                variableDeclarator.getName().asString()
        );
        ExpressionStmt expressionStmt = new ExpressionStmt(variableDeclarationExpr);
        expressionStmt.setComment(new LineComment("ARTool: Body of the method called transferred into call position."));
        return expressionStmt;
    }


}

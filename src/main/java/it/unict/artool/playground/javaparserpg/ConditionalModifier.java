package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import it.unict.artool.util.CodeGenerationUtil;

import java.util.*;


public class ConditionalModifier extends ModifierVisitor<Map<BlockStmt, List<IfStmt>>> {

    protected static Map<IfStmt, Boolean> ifStmtBooleanMap;

    public static void initMap() {
        ifStmtBooleanMap = new HashMap<>();
    }

    @Override
    public Visitable visit(BlockStmt b, Map<BlockStmt, List<IfStmt>> arg) {
        arg.put(b, new ArrayList<>());
        if (Objects.nonNull(b.getChildNodes())) {
            for (Node c : b.getChildNodes()) {
                if (c instanceof IfStmt) {
                    ifStmtBooleanMap.put((IfStmt) c, true);
                    if (!arg.get(b).contains((IfStmt) c)) {
                        arg.get(b).add((IfStmt) c);
                    }
                    ;
                    checkMultipleIf((IfStmt) c, b, arg);
                }
            }
        }
        if (arg.get(b).isEmpty()) {
            arg.remove(b);
        }
        return super.visit(b, arg);
    }

    private void checkMultipleIf(IfStmt n, BlockStmt b, Map<BlockStmt, List<IfStmt>> arg) {
        for (Node c : b.getChildNodes()) {
            if (c instanceof IfStmt) {
                if (!n.equals(c)) {
                    if (Objects.isNull(ifStmtBooleanMap.get(c))) {
                        ifStmtBooleanMap.put((IfStmt) c, true);
                        arg.get(b).add((IfStmt) c);
                    }
                }
            }
        }
    }

    public List<BlockStmt> generateSwitchVariant(CompilationUnit cu, Map<BlockStmt, List<IfStmt>> blockStmtListMap) {
        Node testNode = new LineComment("test");
        for (BlockStmt b : blockStmtListMap.keySet()) {
            for (IfStmt is : blockStmtListMap.get(b)) {
                cu.getLocalDeclarationFromClassname(cu.getPrimaryTypeName().get()).get(0).getMethods().get(0).getBody().get().getStatements().remove(is);
//                cu.getLocalDeclarationFromClassname(cu.getPrimaryTypeName().get()).get(0).getMethods().get(0).getBody().get().getStatements().remove(is);
            }
            //TODO: localizzare i blocchi contenenti gli ifsmt ripetuti su stessa variabile e commutarli in switch
            //TODO: eliminare gli ifstms e inserire uno switchstmt unico
//            SwitchStmt switchStmt=new SwitchStmt();
//            SwitchEntry switchEntry=new SwitchEntry();
//            fd.getVariables().forEach(v ->
//                    v.getInitializer().ifPresent(i ->
//                            i.ifIntegerLiteralExpr(il ->
//                                    v.setInitializer(formatWithUnderscores(il.getValue(), il.getRange()))
//                            )
//                    )
//            );
//            return fd;
        }
        CodeGenerationUtil.outputVariant(cu);
        return null;
    }

}

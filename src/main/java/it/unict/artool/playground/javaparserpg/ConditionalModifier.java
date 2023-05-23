package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

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

    public static List<BlockStmt> getSwitchSuggestion(Map<BlockStmt, List<IfStmt>> blockStmtListMap) {
        //TODO
        return null;
    }

}

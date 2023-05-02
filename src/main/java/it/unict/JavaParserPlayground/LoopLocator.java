package it.unict.JavaParserPlayground;


import com.github.javaparser.Range;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LoopLocator extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(ForEachStmt n, Void arg) {
        super.visit(n, arg);
        analyzeLoop(n, n.getRange());
    }

    private void analyzeLoop(ForEachStmt fes, Optional<Range> range) {
        int line = -1;
        if (range.isPresent()) {
            line = range.get().begin.line;
        }
        log.info("SUGGESTION [Java8+] - LINE[{}]: change ForEach loop with forEach method from Iterable Java interface to improve readability:", line >= 0 ? line : "?");
        log.info("---OLD VERSION---");
        log.info("\n" + fes.toString());
        log.info("---NEW VERSION---");
        log.info("\n" + fes.getIterable() + ".forEach(" + getStringFromVariableList(fes.getVariable().getVariables()) + " -> " + fes.getBody() + ");");
    }

    private String getStringFromVariableList(NodeList<VariableDeclarator> variableList) {
        return variableList.isNonEmpty() ? variableList.get(0).toString() : "?";
    }

}

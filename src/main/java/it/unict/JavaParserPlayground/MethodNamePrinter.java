package it.unict.JavaParserPlayground;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MethodNamePrinter extends VoidVisitorAdapter<Void> {

    protected static List<String> methodList;

    @Override
    public void visit(MethodDeclaration md, Void arg) {
        super.visit(md, arg);
        methodList.add(md.getName().toString());
    }

    public static List<String> getMethodList(CompilationUnit cu) {
        methodList = new ArrayList<>();
        VoidVisitor<Void> methodNameVisitor = new MethodNamePrinter();
        methodNameVisitor.visit(cu, null);
        return methodList;
    }

}
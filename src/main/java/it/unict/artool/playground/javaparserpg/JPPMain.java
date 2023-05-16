package it.unict.artool.playground.javaparserpg;

import it.unict.artool.util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JPPMain {

    private static final String CYCLE_SAMPLE_PATH = "src/main/java/it/unict/artool/playground/codesamples/CycleSample.java";

    private static final String INTEGER_SAMPLE_PATH = "src/main/java/it/unict/artool/playground/codesamples/IntegerModifySample.java";

    private static final String COMMENT_SAMPLE_PATH = "src/main/java/it/unict/artool/playground/codesamples/CommentSample.java";

    public static void main(String[] args) {
        LoggerUtil.logMethodStart(log);

        JavaParserPlayground javaParserPlayground = new JavaParserPlayground();

        // printMethods(String filePath) call
        javaParserPlayground.printMethods(CYCLE_SAMPLE_PATH);

        // getMethodListFromVisitor(String filePath) call
        javaParserPlayground.printMethodListUsingVisitor(CYCLE_SAMPLE_PATH);

        // printIntegerLiteralWithModifier(String filePath) call
        javaParserPlayground.printIntegerLiteralWithModifier(INTEGER_SAMPLE_PATH);

        // localeLoops(String filePath) call
        javaParserPlayground.locateLoops(CYCLE_SAMPLE_PATH);

        // printComments(String filePath) call
        javaParserPlayground.printComments(COMMENT_SAMPLE_PATH);

        LoggerUtil.logMethodEnd(log);
    }

}

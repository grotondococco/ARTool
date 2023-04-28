package it.unict.JavaParserPlayground;

import it.unict.Util.LoggerUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JPPMain {

    private static final String FILE_PATH = "src/main/java/it/unict/JavaCodeSamples/CycleSample.java";

    public static void main(String[] args) {
        LoggerUtil.logMethodStart(log);

        JavaParserPlayground javaParserPlayground = new JavaParserPlayground();

        // printMethods(String filePath) call
        javaParserPlayground.printMethods(FILE_PATH);

        // getMethodListFromVisitor(String filePath) call
        javaParserPlayground.printMethodListUsingVisitor(FILE_PATH);

        LoggerUtil.logMethodEnd(log);
    }

}

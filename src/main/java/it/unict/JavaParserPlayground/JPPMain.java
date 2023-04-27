package it.unict.JavaParserPlayground;

public class JPPMain {
    private static final String FILE_PATH = "src/main/java/it/unict/JavaCodeSamples/CycleSample.java";

    public static void main(String[] args) {
        JavaParserPlayground javaParserPlayground = new JavaParserPlayground();
        javaParserPlayground.printMethods(FILE_PATH);
    }

}

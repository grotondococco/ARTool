package it.unict.artool.enums;

public enum AlgorithmRecognition {
    CONDITIONAL("CONDITIONAL: Check multiple If Statements over the same left expression and operator then convert it into a Switch Statement."),
    LIST("LIST: Covert LinkedList in ArrayList."),
    SET("SET: Convert TreeSet in HashSet."),
    SINGLEMETHODCALL("SINGLEMETHODCALL: Transfers the body of methods called once at the calling position.");
    private final String description;

    AlgorithmRecognition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

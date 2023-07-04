package it.unict.artool.enums;

public enum AlgorithmRecognition {
    CONDITIONAL("CONDITIONAL: Check multiple if statements on the same argument and convert it into switch."),
    ARRAY("ARRAY: Check if array are used and convert the usage into List."),
    LIST("LIST: Covert LinkedList in ArrayList."),
    SET("SET: Convert TreeSet in HashSet."),
    SINGLEMETHODCALL("SINGLEMETHODCALL: Transfers the body of methods called once at the calling position."),
    BESTCOLLECTION("BESTCOLLECTION: decide best collection usage for the operations type utilized.");
    private final String description;

    AlgorithmRecognition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

package it.unict.Enum;

public enum ERRORS {

    FILE_NOT_FOUND("Input file not found.");

    private ERRORS(final String description) {
        this.descrption = description;
    }

    private final String descrption;

    public String getDescrption() {
        return descrption;
    }

}

package it.unict.artool.enums;

public enum Errors {

    FILE_NOT_FOUND("Input file not found.");

    private Errors(final String description) {
        this.descrption = description;
    }

    private final String descrption;

    public String getDescrption() {
        return descrption;
    }

}

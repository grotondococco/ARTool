package it.unict.artool.enums;

public enum Errors {

    GENERIC("An error occurred"), FILE_NOT_FOUND("Input file not found."), FILE_ALREADY_EXISTS("Output file already exists");

    private Errors(final String description) {
        this.descrption = description;
    }

    private final String descrption;

    public String getDescrption() {
        return descrption;
    }

}

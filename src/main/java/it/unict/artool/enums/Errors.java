package it.unict.artool.enums;

public enum Errors {

    GENERIC("An error occurred"), FILE_NOT_FOUND("Input file not found."), ALGORITHM_NOT_SUPPORTED("Selected algorithm recognition not supported");

    Errors(final String description) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }

}

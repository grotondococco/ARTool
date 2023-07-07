package it.unict.artool.javaparserpg;

import lombok.Data;

@Data
public class CommentReportEntry {

    private String type;
    private String text;
    private int lineNumber;
    private boolean isOrphan;

    CommentReportEntry(String type, String text, int lineNumber, boolean isOrphan) {
        this.type = type;
        this.text = text;
        this.lineNumber = lineNumber;
        this.isOrphan = isOrphan;
    }

    @Override
    public String toString() {
        return "LINE[" + lineNumber + "]|" + type + "|" + isOrphan + "|" + text.replace("\\n", " ").trim();
    }
}
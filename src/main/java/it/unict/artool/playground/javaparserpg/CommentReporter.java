package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import it.unict.artool.enums.Errors;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CommentReporter {

    public List<CommentReportEntry> getAllComments(String filePath) {
        CompilationUnit cu = null;
        try {
            cu = StaticJavaParser.parse(new File(filePath));
        } catch (FileNotFoundException e) {
            log.error(Errors.FILE_NOT_FOUND.getDescription());
        }
        List<Comment> allContainedComments = cu != null ? cu.getAllContainedComments() : null;
        if (Objects.isNull(allContainedComments)) {
            return new ArrayList<>();
        }
        return cu.getAllContainedComments().stream().map(p -> new CommentReportEntry(p.getClass().getSimpleName(), p.getContent(), p.getRange().map(r -> r.begin.line).orElse(-1), p.getCommentedNode().isPresent())).toList();
    }

}

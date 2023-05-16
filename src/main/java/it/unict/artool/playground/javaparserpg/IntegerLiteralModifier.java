package it.unict.artool.playground.javaparserpg;

import com.github.javaparser.Range;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
public class IntegerLiteralModifier extends ModifierVisitor<Void> {

    private static final Pattern LOOK_AHEAD_THREE = Pattern.compile("(\\d)(?=(\\d{3})+$)");

    @Override
    public FieldDeclaration visit(FieldDeclaration fd, Void arg) {
        super.visit(fd, arg);
        fd.getVariables().forEach(v ->
                v.getInitializer().ifPresent(i ->
                        i.ifIntegerLiteralExpr(il ->
                                v.setInitializer(formatWithUnderscores(il.getValue(), il.getRange()))
                        )
                )
        );
        return fd;
    }

    static String formatWithUnderscores(String value, Optional<Range> range) {
        String withoutUnderscores = value.replaceAll("_", "");
        String formattedValue = LOOK_AHEAD_THREE.matcher(withoutUnderscores).replaceAll("$1_");
        int line = -1;
        if (range.isPresent()) {
            line = range.get().begin.line;
        }
        if (!value.equals(formattedValue)) {
            log.info("SUGGESTION [Java7+] - LINE[{}]: change numerical value {} -----> {} to improve readability.", line >= 0 ? line : "?", value, formattedValue);
        }
        return formattedValue;
    }


}

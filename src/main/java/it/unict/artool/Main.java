package it.unict.artool;

import it.unict.artool.core.Artool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("ARTool Demo Start");
        Artool.generateVariant();
    }

}
package it.unict.Util;

import org.slf4j.Logger;

public class LoggerUtil {
    public static void logMethodStart(Logger log){
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        log.debug("{} - Method START", stackTrace[1]);
    }

    public static void logMethodEnd(Logger log){
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        log.debug("{} - Method END", stackTrace[1]);
    }
}

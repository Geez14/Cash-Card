package com.geez14.app.util;

final public class Debug {
    private final static String ERROR = "\033[91m"; // red
    private final static String LOG = "\033[92m"; //
    private final static String WARN = "\033[93m"; // yellow
    private final static String INFO = "\033[94m"; // blue
    private final static String OK = "\033[95m"; // pink
    private final static String RESET = "\033[0m"; // reset

    static class Helper {
        private static void generalMessage(String color, Object... messages) {
            System.out.print(color);
            System.out.println("[-----------------------------Special Messages-----------------------------]");
            for (Object message : messages) {
                System.out.print("-> ");
                System.out.println(message);
            }
            System.out.println("[----------------------------------.END-------------------------------------]");
            System.out.println(RESET);
        }
        private static void generalMessage(String color, String key, String message) {
            generalMessage(color, key.concat(":\n\t").concat(message));
        }
    }


    public static void warn(String key, String message) {
        Helper.generalMessage(WARN, key, message);
    }

    public static void warn(Object... messages) {
        Helper.generalMessage(WARN, messages);
    }

    public static void error(String key, String message) {
        Helper.generalMessage(ERROR, key, message);
    }

    public static void error(Object... messages) {
        Helper.generalMessage(ERROR, messages);
    }

    public static void ok(String key, String message) {
        Helper.generalMessage(WARN, key, message);
    }

    public static void ok(Object... messages) {
        Helper.generalMessage(OK, messages);
    }

    public static void log(String key, String message) {
        Helper.generalMessage(LOG, key, message);
    }

    public static void log(Object... messages) {
        Helper.generalMessage(LOG, messages);
    }

    public static void debug(String key, String message) {
        Helper.generalMessage(INFO, key, message);
    }

    public static void debug(Object... messages) {
        Helper.generalMessage(INFO, messages);
    }
}

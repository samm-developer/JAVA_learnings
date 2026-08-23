// Shared colored headings for lesson output (terminal ANSI colors).
// Compile with any lesson: javac LessonConsole.java Lesson13c_HashMapDeepDive.java
//
// Colors:
//   section  (=== 0), === 1) ...)  → cyan
//   PROBLEM                          → red
//   SOLUTION                         → green
//   BONUS                            → magenta
//   Summary                          → yellow
//
// Disable colors: NO_COLOR=1 java Lesson13c_HashMapDeepDive

public final class LessonConsole {

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    private LessonConsole() { }

    /** Auto-pick color from heading text. Use for all "=== ..." section lines. */
    public static void heading(String message) {
        if (message.contains("PROBLEM:")) {
            problem(message);
        } else if (message.contains("SOLUTION:")) {
            solution(message);
        } else if (message.contains("BONUS:")) {
            bonus(message);
        } else if (message.contains("Summary:")) {
            summary(message);
        } else {
            section(message);
        }
    }

    public static void section(String message) {
        println(CYAN, message);
    }

    public static void problem(String message) {
        println(RED, message);
    }

    public static void solution(String message) {
        println(GREEN, message);
    }

    public static void bonus(String message) {
        println(MAGENTA, message);
    }

    public static void summary(String message) {
        println(YELLOW, message);
    }

    private static void println(String color, String message) {
        if (useColor()) {
            System.out.println(BOLD + color + message + RESET);
        } else {
            System.out.println(message);
        }
    }

    private static boolean useColor() {
        String noColor = System.getenv("NO_COLOR");
        return noColor == null || noColor.isBlank();
    }
}

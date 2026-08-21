// Lesson 22b: Switch expressions + pattern matching (Java 14/21)
// Compile: javac Lesson22b_SwitchExpressions.java
// Run:     java Lesson22b_SwitchExpressions
//
// Place in curriculum: after records / modern syntax (Lesson 21–22).

public class Lesson22b_SwitchExpressions {
    public static void main(String[] args) {

        // ===== 1) Old switch (statement) — fall-through with break =====
        int day = 3;
        String oldStyle;
        switch (day) {
            case 1:
                oldStyle = "Mon";
                break;
            case 2:
                oldStyle = "Tue";
                break;
            case 3:
                oldStyle = "Wed";
                break;
            default:
                oldStyle = "?";
        }
        System.out.println("Old switch: " + oldStyle);

        // ===== 2) Switch EXPRESSION — returns a value, uses -> =====
        String name = switch (day) {
            case 1 -> "Mon";
            case 2 -> "Tue";
            case 3 -> "Wed";
            case 4, 5 -> "Thu/Fri"; // multiple labels
            default -> "Weekend-ish";
        };
        System.out.println("Switch expression: " + name);

        // ===== 3) yield — when case needs a block =====
        int score = 87;
        String grade = switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> {
                System.out.println("(block for B)");
                yield "B";
            }
            case 7 -> "C";
            case 6 -> "D";
            default -> "F";
        };
        System.out.println("Grade: " + grade);

        // ===== 4) Pattern matching for instanceof (Java 16+) =====
        Object value = "Hello Java";
        if (value instanceof String s) {
            // s is already a String — no cast needed
            System.out.println("Upper: " + s.toUpperCase());
        }

        // ===== 5) Pattern matching in switch (Java 21) =====
        Object[] items = {42, "hi", 3.14, true, null};
        for (Object item : items) {
            String desc = switch (item) {
                case null -> "null";
                case Integer i when i > 0 -> "positive int " + i;
                case Integer i -> "int " + i;
                case String s -> "string len=" + s.length();
                case Double d -> "double " + d;
                case Boolean b -> "bool " + b;
                default -> "other: " + item;
            };
            System.out.println(desc);
        }
    }
}

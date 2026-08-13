// Lesson 4: if / else (decision making)
// Compile: javac Lesson04_IfElse.java
// Run:     java Lesson04_IfElse
// Or:      java Lesson04_IfElse 85

public class Lesson04_IfElse {
    public static void main(String[] args) {

        // Use command-line score if given, otherwise default
        int score = 75;
        if (args.length > 0) {
            score = Integer.parseInt(args[0]); // text → int
        }

        System.out.println("Score: " + score);

        // ===== Simple if =====
        if (score >= 40) {
            System.out.println("You passed!");
        }

        // ===== if - else =====
        if (score >= 40) {
            System.out.println("Status: PASS");
        } else {
            System.out.println("Status: FAIL");
        }

        // ===== if - else if - else (grade bands) =====
        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 75) {
            System.out.println("Grade: B");
        } else if (score >= 60) {
            System.out.println("Grade: C");
        } else if (score >= 40) {
            System.out.println("Grade: D");
        } else {
            System.out.println("Grade: F");
        }

        // ===== Combining conditions =====
        int age = 20;
        boolean hasTicket = true;

        if (age >= 18 && hasTicket) {
            System.out.println("Welcome to the movie!");
        } else {
            System.out.println("Entry denied.");
        }

        // ===== Nested if =====
        boolean isRaining = true;
        boolean hasUmbrella = false;

        if (isRaining) {
            if (hasUmbrella) {
                System.out.println("Go out with umbrella.");
            } else {
                System.out.println("Stay home or get wet!");
            }
        } else {
            System.out.println("Nice day — go out!");
        }

        // ===== Ternary operator (short if-else) =====
        // condition ? valueIfTrue : valueIfFalse
        String result = (score >= 40) ? "Pass" : "Fail";
        System.out.println("Ternary result: " + result);

        // ===== Equality tip for Strings =====
        String day = "Monday";
        // Use .equals() for String content, NOT ==
        if (day.equals("Monday")) {
            System.out.println("Start of the week.");
        }
    }
}

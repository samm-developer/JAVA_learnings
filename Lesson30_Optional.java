// Lesson 30: Optional (handle missing values safely)
// Compile: javac Lesson30_Optional.java
// Run:     java Lesson30_Optional

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Lesson30_Optional {
    public static void main(String[] args) {
        Map<String, Integer> marks = new HashMap<>();
        marks.put("Asha", 92);
        marks.put("Ravi", 75);
        // Neha is missing on purpose

        // ===== Old style: null checks =====
        Integer nehaOld = marks.get("Neha"); // null
        if (nehaOld != null) {
            System.out.println("Neha: " + nehaOld);
        } else {
            System.out.println("Neha not found (null check)");
        }

        // ===== Optional style =====
        Optional<Integer> neha = findMarks(marks, "Neha");
        Optional<Integer> asha = findMarks(marks, "Asha");

        System.out.println("Neha isPresent? " + neha.isPresent());
        System.out.println("Asha isPresent? " + asha.isPresent());

        // ifPresent — run only when value exists
        asha.ifPresent(m -> System.out.println("Asha scored " + m));

        // orElse — default if missing
        int nehaMarks = neha.orElse(0);
        System.out.println("Neha marks (orElse 0): " + nehaMarks);

        // orElseGet — default from a supplier (lazy)
        int ravi = findMarks(marks, "Ravi").orElseGet(() -> -1);
        System.out.println("Ravi: " + ravi);

        // map — transform if present
        Optional<String> grade = asha.map(Lesson30_Optional::toGrade);
        System.out.println("Asha grade: " + grade.orElse("N/A"));

        // Practical: print all requested names safely
        System.out.println("--- lookup list ---");
        for (String name : new String[] {"Asha", "Neha", "Ravi", "Shashwat"}) {
            findMarks(marks, name).ifPresentOrElse(
                    m -> System.out.println(name + " => " + m),
                    () -> System.out.println(name + " => not found")
            );
        }
    }

    // Return Optional instead of null
    static Optional<Integer> findMarks(Map<String, Integer> marks, String name) {
        Integer value = marks.get(name);
        return Optional.ofNullable(value); // empty if null, else wraps value
    }

    static String toGrade(int marks) {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        return "D";
    }
}

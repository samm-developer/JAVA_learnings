// Lesson 22d: Local record (record inside a method)
// Compile: javac LessonConsole.java Lesson22d_LocalRecord.java
// Run:     java Lesson22d_LocalRecord
//
// After: Lesson 22 (Records), 22c (old class vs record)
// Java 16+ — record declared inside a method body (local scope only)
//
// ========== CHEAT SHEET ==========
// Local record = short-lived data shape used only inside one method
// Same auto: fields, constructor, equals, hashCode, toString, accessors
// Scope: only inside the method where declared — cannot return the type outside
// Use when: grouping values temporarily — no extra top-level class file

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Lesson22d_LocalRecord {

    public static void main(String[] args) {
        problemWithoutLocalRecord();
        solutionLocalRecord();
        inLoopAndStream();
        whenToUse();
        rulesAndLimits();
        summary();
    }

    static void problemWithoutLocalRecord() {
        LessonConsole.heading("=== PROBLEM: two related values as separate vars ===");
        String line = "Asha:92";
        String[] parts = line.split(":");
        String name = parts[0];
        int score = Integer.parseInt(parts[1]);
        System.out.println("  name=" + name + " score=" + score);
        System.out.println("  easy to pass wrong order, lose pairing ❌");
        System.out.println();
    }

    static void solutionLocalRecord() {
        LessonConsole.heading("=== SOLUTION: local record — group data in one type ===");

        // declared inside method — exists only here
        record ParsedLine(String name, int score) {
            String label() {
                return name + " scored " + score;
            }
        }

        String line = "Riya:85";
        String[] parts = line.split(":");
        ParsedLine parsed = new ParsedLine(parts[0], Integer.parseInt(parts[1]));

        System.out.println("  " + parsed);
        System.out.println("  " + parsed.label() + "  ✅");
        System.out.println();
    }

    static void inLoopAndStream() {
        LessonConsole.heading("=== 2) Local record with list + sort ===");

        record ParsedLine(String name, int score) { }

        List<String> lines = List.of("Asha:92", "Dev:78", "Riya:88");
        List<ParsedLine> results = new ArrayList<>();

        for (String line : lines) {
            String[] p = line.split(":");
            results.add(new ParsedLine(p[0], Integer.parseInt(p[1])));
        }

        results.sort(Comparator.comparingInt(ParsedLine::score).reversed());
        System.out.println("  top: " + results.get(0));
        System.out.println("  all: " + results);
        System.out.println();
    }

    static void whenToUse() {
        LessonConsole.heading("=== 3) When to USE a local record ===");
        System.out.println("""
                  USE local record when data is needed ONLY inside ONE method:

                  ✅ Parse result       → split "name:score" into one object
                  ✅ Loop accumulator   → collect List<ParsedLine> in same method
                  ✅ Stream intermediate → map rows to a temp shape, sort, filter
                  ✅ Algorithm helper   → pair/index tuple used in one place only

                  DON'T use when:
                  ❌ Other methods need the type     → top-level record (22)
                  ❌ Type belongs to outer concept   → nested record (22e)
                  ❌ Return type of a public API     → top-level or nested
                """);

        // real use: stream + local record — no extra class file
        record ParsedLine(String name, int score) { }

        List<String> lines = List.of("Dev:78", "Asha:92", "Riya:88");
        List<ParsedLine> passing = lines.stream()
                .map(line -> {
                    String[] p = line.split(":");
                    return new ParsedLine(p[0], Integer.parseInt(p[1]));
                })
                .filter(p -> p.score() >= 80)
                .sorted(Comparator.comparingInt(ParsedLine::score).reversed())
                .toList();

        System.out.println("  passing (score >= 80): " + passing);
        System.out.println();
    }

    static void rulesAndLimits() {
        LessonConsole.heading("=== 4) Rules & limits ===");
        System.out.println("""
                ✅ Can:     fields, methods, implement interfaces, use in same method
                ❌ Cannot:  use ParsedLine type outside this method
                           return ParsedLine from method (type not visible outside)
                           make it public top-level — use normal record (Lesson 22) instead

                Top-level record  → reused project-wide (Student, Task, Point)
                Local record      → one method's helper (parse result, temp pair)
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Local record ===");
        System.out.println("""
                Syntax:  inside method →  record Name(Type a, Type b) { }
                When:    temporary grouped data — no new file / top-level class
                See:     Lesson 22_Records for top-level, 22e for nested records
                """);
    }
}

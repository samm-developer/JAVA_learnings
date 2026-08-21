// Lesson 22c: Before records (boilerplate) vs after records
// Compile: javac Lesson22c_OldClassVsRecord.java
// Run:     java Lesson22c_OldClassVsRecord
//
// Place: right after Lesson 22 (records).
// Yes — records remove most getter/constructor/equals/toString boilerplate.
// Note: records are IMMUTABLE → no setters; "change" = create a new record.

import java.util.Objects;

// =============================================================================
// BEFORE Java 16 — normal class with lots of boilerplate
// =============================================================================
class StudentOld {
    private final String name;
    private final int marks;

    StudentOld(String name, int marks) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("marks must be 0..100");
        }
        this.name = name;
        this.marks = marks;
    }

    String getName() {
        return name;
    }

    int getMarks() {
        return marks;
    }

    // No setters here (immutable style) — same idea as a record.
    // Mutable classes would also add setName / setMarks (even more code).

    String grade() {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentOld other)) return false;
        return marks == other.marks && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, marks);
    }

    @Override
    public String toString() {
        return "StudentOld[name=" + name + ", marks=" + marks + "]";
    }
}

// =============================================================================
// AFTER — record (same data + behavior, almost no boilerplate)
// =============================================================================
record StudentNew(String name, int marks) {
    // Compact constructor = validation only (fields assigned automatically)
    StudentNew {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("marks must be 0..100");
        }
    }

    String grade() {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }
}

public class Lesson22c_OldClassVsRecord {
    public static void main(String[] args) {
        System.out.println("=== BEFORE (boilerplate class) ===");
        StudentOld old1 = new StudentOld("Asha", 90);
        StudentOld old2 = new StudentOld("Asha", 90);
        System.out.println("getName():  " + old1.getName());
        System.out.println("getMarks(): " + old1.getMarks());
        System.out.println("grade():    " + old1.grade());
        System.out.println("toString:   " + old1);
        System.out.println("equals:     " + old1.equals(old2));

        System.out.println();
        System.out.println("=== AFTER (record) ===");
        StudentNew neu1 = new StudentNew("Asha", 90);
        StudentNew neu2 = new StudentNew("Asha", 90);
        System.out.println("name():     " + neu1.name());   // not getName()
        System.out.println("marks():    " + neu1.marks());  // not getMarks()
        System.out.println("grade():    " + neu1.grade());
        System.out.println("toString:   " + neu1);          // auto
        System.out.println("equals:     " + neu1.equals(neu2)); // auto

        System.out.println();
        System.out.println("=== Immutable \"update\" (both styles) ===");
        // No setMarks(...) — create a new object with new marks
        StudentOld oldUpdated = new StudentOld(old1.getName(), 95);
        StudentNew neuUpdated = new StudentNew(neu1.name(), 95);
        System.out.println("OLD updated: " + oldUpdated);
        System.out.println("NEW updated: " + neuUpdated);

        System.out.println();
        System.out.println("Summary:");
        System.out.println("  Old class → write fields, ctor, getters, equals, hashCode, toString");
        System.out.println("  Record    → one line header; those are generated for you");
        System.out.println("  Accessor  → getName()  vs  name()");
        System.out.println("  Setters   → records have none (immutable data carrier)");
    }
}

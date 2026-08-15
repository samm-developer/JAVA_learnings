// Lesson 22: record (short data class)
// Compile: javac Lesson22_Records.java
// Run:     java Lesson22_Records

// Old style data class needs lots of boilerplate:
// fields, constructor, getters, toString, equals...

// record = compact data carrier
record Point(int x, int y) {
}

record Student(String name, int marks) {
    // Compact constructor: validate before fields are set
    Student {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("marks must be 0..100");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name required");
        }
    }

    // Extra instance method is allowed
    String grade() {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }
}

public class Lesson22_Records {
    public static void main(String[] args) {
        Point p = new Point(3, 4);
        System.out.println(p);          // Point[x=3, y=4]  (auto toString)
        System.out.println("x = " + p.x()); // accessor is x(), not getX()
        System.out.println("y = " + p.y());

        Point p2 = new Point(3, 4);
        System.out.println("p.equals(p2)? " + p.equals(p2)); // true (auto equals)

        Student s = new Student("Shashwat", 98);
        System.out.println(s);
        System.out.println(s.name() + " grade = " + s.grade());

        // Records are immutable: no s.marks = 99;
        // You create a new one if you need a change:
        Student updated = new Student(s.name(), 100);
        System.out.println("updated: " + updated);

        try {
            new Student("", 50);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation: " + e.getMessage());
        }
    }
}

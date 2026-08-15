// Lesson 21: Enums (fixed set of named values)
// Compile: javac Lesson21_Enums.java
// Run:     java Lesson21_Enums

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

enum OrderStatus {
    PENDING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

// Enum with fields + methods
enum Grade {
    A(90), B(75), C(60), D(40), F(0);

    private final int minMarks;

    Grade(int minMarks) {
        this.minMarks = minMarks;
    }

    public int getMinMarks() {
        return minMarks;
    }

    public static Grade fromMarks(int marks) {
        if (marks >= A.minMarks) return A;
        if (marks >= B.minMarks) return B;
        if (marks >= C.minMarks) return C;
        if (marks >= D.minMarks) return D;
        return F;
    }
}

public class Lesson21_Enums {
    public static void main(String[] args) {
        Day today = Day.SATURDAY;
        System.out.println("Today: " + today);

        // switch works great with enums
        switch (today) {
            case SATURDAY, SUNDAY -> System.out.println("Weekend!");
            default -> System.out.println("Weekday — study Java.");
        }

        // Loop all values
        System.out.println("--- all days ---");
        for (Day d : Day.values()) {
            System.out.println(d);
        }

        // Order status example
        OrderStatus status = OrderStatus.PENDING;
        System.out.println("Order: " + status);
        status = OrderStatus.SHIPPED;
        System.out.println("Order updated: " + status);

        // Compare with == (safe for enums)
        if (status == OrderStatus.SHIPPED) {
            System.out.println("Package is on the way.");
        }

        // Grade from marks
        int marks = 88;
        Grade g = Grade.fromMarks(marks);
        System.out.println("Marks " + marks + " => Grade " + g
                + " (min " + g.getMinMarks() + ")");

        // Why enum instead of String?
        // String status2 = "SHIPED"; // typo compiles, bug later
        // OrderStatus status3 = OrderStatus.SHIPED; // compile error — good!
    }
}

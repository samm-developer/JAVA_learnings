// Lesson 22e: Nested record (record inside another record or class)
// Compile: javac LessonConsole.java Lesson22e_NestedRecord.java
// Run:     java Lesson22e_NestedRecord
//
// After: Lesson 22 (Records), 22d (local record)
// Java 16+ — group related types: Outer.Inner
//
// ========== CHEAT SHEET ==========
// Nested record = record declared inside another record or class
// Access:       Outer.Inner  e.g. Order.LineItem
// Inside class: implicitly static (no outer instance needed)
// Use when:     inner type belongs to outer domain — Order + LineItem

import java.util.List;

public class Lesson22e_NestedRecord {

    // nested at class level — implicitly static; visible as Lesson22e_NestedRecord.Address
    record Address(String city, String pin) { }

    record Person(String name, Address home) { }

    public static void main(String[] args) {
        problemFlatFields();
        solutionNestedRecord();
        nestedInsideClass();
        diagram();
        rulesAndLimits();
        summary();
    }

    static void problemFlatFields() {
        LessonConsole.heading("=== PROBLEM: related types scattered ===");
        System.out.println("""
                  Order needs line items — each item has product, qty, price
                  Flat approach: separate top-level records or many loose fields

                  record LineItem(...)   // file clutter — only Order uses this
                  record Order(..., LineItem[] items)

                  Or worse: parallel arrays / String blobs — easy to mismatch ❌
                """);
    }

    static void solutionNestedRecord() {
        LessonConsole.heading("=== SOLUTION: nested record — inner type lives inside outer ===");

        record Order(String orderId, List<LineItem> items) {
            record LineItem(String product, int qty, double price) {
                double lineTotal() {
                    return qty * price;
                }
            }

            double orderTotal() {
                return items.stream().mapToDouble(LineItem::lineTotal).sum();
            }
        }

        Order.LineItem pen = new Order.LineItem("Pen", 2, 10.0);
        Order.LineItem book = new Order.LineItem("Book", 1, 250.0);
        Order order = new Order("ORD-101", List.of(pen, book));

        System.out.println("  orderId: " + order.orderId());
        System.out.println("  item 1:  " + pen + "  total=" + pen.lineTotal());
        System.out.println("  item 2:  " + book + "  total=" + book.lineTotal());
        System.out.println("  order total: " + order.orderTotal() + "  ✅");
        System.out.println();
    }

    static void nestedInsideClass() {
        LessonConsole.heading("=== 2) Nested record inside a class (implicitly static) ===");

        Person p = new Person("Asha", new Address("Pune", "411001"));
        System.out.println("  " + p.name() + " lives in " + p.home().city());
        System.out.println("  type: " + Person.class.getSimpleName()
                + " with " + Address.class.getSimpleName());
        System.out.println("  full: " + p);
        System.out.println();
    }

    static void diagram() {
        LessonConsole.heading("=== 3) Structure diagram ===");
        System.out.println("""
                  record Order(...) {
                      record LineItem(...) { }   ← nested inside Order
                  }

                  Order
                  ├── orderId: "ORD-101"
                  └── items: List<LineItem>
                        ├── LineItem(product=Pen,  qty=2, price=10)
                        └── LineItem(product=Book, qty=1, price=250)

                  Type names:
                    Order          → outer record
                    Order.LineItem → nested record (Outer.Inner)
                """);
    }

    static void rulesAndLimits() {
        LessonConsole.heading("=== 4) Rules & limits ===");
        System.out.println("""
                ✅ Can:     nest record inside record or class
                           custom methods on nested record (lineTotal)
                           use Outer.Inner from outside (Order.LineItem)
                ❌ Cannot:  nest inside a method — that's a LOCAL record (22d)

                Nested record   → reusable type, scoped to outer domain (Order.LineItem)
                Local record    → one method only, invisible outside (22d)
                Top-level record → project-wide (Student, Point in Lesson 22)
                """);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Nested record ===");
        System.out.println("""
                Syntax:  record Outer(...) { record Inner(...) { } }
                Access:  Outer.Inner obj = new Outer.Inner(...)
                When:    inner data belongs to outer concept — keep types together
                See:     22_Records (top-level), 22d (local record)
                """);
    }
}
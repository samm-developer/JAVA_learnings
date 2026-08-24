// Lesson 08b: Stack vs Heap memory — where variables and objects live
// Compile: javac LessonConsole.java Lesson08b_StackAndHeap.java
// Run:     java Lesson08b_StackAndHeap
//
// After: Lesson 08 (Classes & Objects)
// Also see: 08c (Memory Management), 36c (JVM overview), 25b (threads & memory)
//
// ========== CHEAT SHEET ==========
// STACK: method calls, local primitives, references (pointers) — per thread, LIFO
// HEAP:  objects created with new — shared, cleaned by GC when unreachable

public class Lesson08b_StackAndHeap {

    static class Student {
        String name;  // reference field → String object on HEAP
        int marks;    // primitive field → inside Student object on HEAP

        Student(String name, int marks) {
            this.name = name;
            this.marks = marks;
        }
    }

    public static void main(String[] args) {
        bigPicture();
        primitivesOnStack();
        objectOnHeap();
        twoRefsOneObject();
        methodFrames();
        summary();
    }

    static void bigPicture() {
        LessonConsole.heading("=== 0) Big picture: STACK vs HEAP ===");
        System.out.println("""
                  ┌──────────────────────────────────────────────┐
                  │ HEAP  (objects — shared)                     │
                  │   Student@1 { name→"Asha", marks=90 }        │
                  │   String "Asha"                              │
                  └──────────────────────────────────────────────┘
                  ┌──────────────────┐
                  │ STACK (main)     │
                  │  s ──────────────┼──► Student@1 on HEAP
                  │  x = 10          │    (primitive stays on stack)
                  └──────────────────┘

                  Rule of thumb:
                    int x = 10;              → 10 lives on STACK
                    Student s = new ...;     → object on HEAP, s (ref) on STACK
                """);
    }

    static void primitivesOnStack() {
        LessonConsole.heading("=== 1) Primitives live on the STACK ===");

        int a = 10;
        int b = a;   // copy the VALUE
        b = 99;      // only b changes

        System.out.println("""
                  STACK (main frame)
                  ┌─────────────┐
                  │ a = 10      │
                  │ b = 99      │   ← b is a separate slot (copy)
                  └─────────────┘
                """);
        System.out.println("  a=" + a + "  b=" + b + "  (a unchanged)  ✅");
        System.out.println();
    }

    static void objectOnHeap() {
        LessonConsole.heading("=== 2) Objects live on the HEAP; ref on STACK ===");

        Student s = new Student("Asha", 90);

        System.out.println("""
                  STACK                         HEAP
                  ┌──────────┐                  ┌─────────────────────────┐
                  │ s ───────┼─────────────────►│ Student                 │
                  └──────────┘                  │   name ──► "Asha"       │
                                                │   marks = 90            │
                                                └─────────────────────────┘
                """);
        System.out.println("  s.name=" + s.name + "  s.marks=" + s.marks);
        System.out.println("  s (reference) → " + s); // prints class@hash by default if no toString
        System.out.println();
    }

    static void twoRefsOneObject() {
        LessonConsole.heading("=== PROBLEM: two refs → ONE object on HEAP ===");

        Student s1 = new Student("Dev", 70);
        Student s2 = s1;   // copy the REFERENCE, not the object

        System.out.println("""
                  STACK                         HEAP
                  ┌──────────┐                  ┌─────────────────────────┐
                  │ s1 ──────┼──┐               │ Student                 │
                  │ s2 ──────┼──┴──────────────►│   name ──► "Dev"        │
                  └──────────┘                  │   marks = 70            │
                                                └─────────────────────────┘
                """);

        s2.marks = 95; // changes THE SAME heap object
        System.out.println("  after s2.marks=95:");
        System.out.println("  s1.marks=" + s1.marks + "  s2.marks=" + s2.marks
                + "  (same object!)  ✅");

        LessonConsole.heading("=== SOLUTION: new object = separate heap slot ===");
        Student s3 = new Student("Dev", 70); // different object
        System.out.println("  s1 == s2 ? " + (s1 == s2) + "  (same reference)");
        System.out.println("  s1 == s3 ? " + (s1 == s3) + "  (different objects)");
        System.out.println();
    }

    static void methodFrames() {
        LessonConsole.heading("=== 3) Each method call pushes a STACK FRAME ===");
        System.out.println("""
                  Call demo(5):

                  STACK (grows down)
                  ┌─────────────────┐
                  │ main            │
                  │   (waiting)     │
                  ├─────────────────┤
                  │ demo frame      │
                  │   n = 5         │  ← local of demo
                  │   result = 15   │
                  └─────────────────┘

                  When demo() returns → its frame is POPPED (locals gone)
                """);

        int result = demo(5);
        System.out.println("  demo(5) returned " + result + "  (frame gone; result copied to main)");
        System.out.println();
    }

    /** Locals n and result live only while this frame is on the stack. */
    static int demo(int n) {
        int result = n * 3;
        return result;
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Stack vs Heap ===");
        System.out.println("""
                  STACK                              HEAP
                  ─────                              ────
                  method frames                      objects (new)
                  local primitives                   arrays
                  references (pointers)              String contents, etc.
                  fast, auto-cleared on return       shared; GC cleans unused
                  per-thread                         one per JVM process

                  Copy primitive  → new value
                  Copy reference  → same object (alias)

                  See also: 08 Classes/Objects · 25b threads & memory · 36c JVM
                """);
    }
}

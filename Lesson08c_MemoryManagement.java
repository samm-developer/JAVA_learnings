// Lesson 08c: Memory Management in Java (core)
// Compile: javac LessonConsole.java Lesson08c_MemoryManagement.java
// Run:     java Lesson08c_MemoryManagement
//
// After: Lesson 08b (Stack vs Heap)
// Also see: 25b (threads & memory), 36c (JVM / heap size flags)
//
// ========== CHEAT SHEET ==========
// STACK  — per thread; locals + references; cleared when method returns
// HEAP   — shared by threads in one JVM; objects from new; cleaned by GC
// CLASS  — loaded once; static fields shared by all instances/threads
// GC     — frees heap objects with no reachable references

public class Lesson08c_MemoryManagement {

    static class Box {
        int value;
        Box(int value) { this.value = value; }
    }

    /** One shared counter for the whole class (all threads / all instances). */
    static int createdCount = 0;

    public static void main(String[] args) {
        overviewDiagram();
        stackVsHeap();
        whoSharesWhat();
        classAndStaticShared();
        gcBasics();
        summary();
    }

    static void overviewDiagram() {
        LessonConsole.heading("=== 0) Memory map (one JVM process) ===");
        System.out.println("""
                  java Lesson08c_MemoryManagement   ← one process / one JVM

                  ┌─────────────────────────────────────────────────────┐
                  │ HEAP (SHARED by all threads)                        │
                  │   objects: new Box(...), String, arrays             │
                  │   Class Box + static createdCount                   │
                  └─────────────────────────────────────────────────────┘
                  ┌──────────────┐  ┌──────────────┐
                  │ STACK main   │  │ STACK worker │  ← NOT shared
                  │  locals      │  │  locals      │
                  │  references  │  │  references  │
                  └──────────────┘  └──────────────┘
                """);
    }

    static void stackVsHeap() {
        LessonConsole.heading("=== 1) STACK vs HEAP ===");

        int n = 7;                 // primitive → STACK
        Box box = new Box(7);      // object → HEAP; box (ref) → STACK
        createdCount++;

        System.out.println("""
                  STACK (main)                 HEAP
                  ┌────────────┐               ┌──────────────────┐
                  │ n = 7      │               │ Box { value=7 }  │
                  │ box ───────┼──────────────►│                  │
                  └────────────┘               └──────────────────┘
                """);
        System.out.println("  n=" + n + "  box.value=" + box.value
                + "  createdCount=" + createdCount);
        System.out.println();
    }

    static void whoSharesWhat() {
        LessonConsole.heading("=== 2) Shared vs NOT shared ===");
        System.out.println("""
                  SHARED (among threads of THIS JVM)
                    • Heap objects (if threads hold a reference)
                    • Class definition + static fields

                  NOT shared
                    • Each thread's STACK (locals, method frames)
                    • Another java process (different JVM = different heap)

                  Example: two refs → one object (shared mutation)
                """);

        Box a = new Box(10);
        Box b = a;          // copy REFERENCE — same heap object
        createdCount++;
        b.value = 99;

        System.out.println("  a.value=" + a.value + "  b.value=" + b.value
                + "  (same object on HEAP)  ✅");
        System.out.println("  a == b ? " + (a == b));
        System.out.println();
    }

    static void classAndStaticShared() {
        LessonConsole.heading("=== 3) Class + static = shared infrastructure ===");
        System.out.println("""
                  CLASS Box          ← ONE copy (shared)
                    static createdCount
                  instances          ← many copies on HEAP
                    Box@1, Box@2, ...
                """);

        Box x = new Box(1);
        Box y = new Box(2);
        createdCount += 2;

        System.out.println("  x.value=" + x.value + "  y.value=" + y.value
                + "  (separate objects)");
        System.out.println("  createdCount=" + createdCount
                + "  (ONE static field for the whole class)  ✅");
        System.out.println();
    }

    static void gcBasics() {
        LessonConsole.heading("=== 4) GC — free unreachable heap objects ===");
        System.out.println("""
                  PROBLEM:  new forever → heap fills → OutOfMemoryError

                  SOLUTION: Garbage Collector (GC)
                    object becomes UNREACHABLE (no live references)
                    → GC may reclaim that heap memory automatically

                  You do NOT free memory by hand (no delete/free in Java).
                """);

        Runtime rt = Runtime.getRuntime();
        long before = usedMb(rt);

        Box temp = new Box(42);   // reachable
        createdCount++;
        System.out.println("  temp.value=" + temp.value + "  (reachable)");

        temp = null;              // drop last reference → unreachable
        System.out.println("  temp = null  → Box may be GC'd later");

        System.gc();              // HINT only — not a guarantee
        long after = usedMb(rt);
        System.out.println("  heap used ~" + before + " MB → ~" + after
                + " MB  (System.gc() is only a hint)");
        System.out.println("""
                  Reachability:
                    live ref on STACK / field / static  → object STAYS
                    no path from GC roots               → object ELIGIBLE for GC
                """);
    }

    static long usedMb(Runtime rt) {
        return (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Memory Management in Java ===");
        System.out.println("""
                  What                    Where         Shared?
                  ────                    ─────         ───────
                  local int / boolean     STACK         no (per thread)
                  reference variable      STACK         no (per thread)
                  object / array          HEAP          yes (via refs)
                  static field            with Class    yes (all threads)
                  class bytecode          loaded once   yes (this JVM)

                  Lifecycle:
                    new → object on HEAP
                    refs keep it alive
                    no refs → GC eligible
                    method return → STACK frame gone (locals gone)

                  Flags (optional):  java -Xms64m -Xmx256m YourClass
                  See: 08b Stack/Heap · 25b threads · 36c JVM basics
                """);
    }
}

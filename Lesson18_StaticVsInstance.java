// Lesson 18: static vs instance
// Compile: javac Lesson18_StaticVsInstance.java
// Run:     java Lesson18_StaticVsInstance

class Counter {
    // ===== INSTANCE field: each object has its OWN copy =====
    int value;

    // ===== STATIC field: ONE copy shared by ALL objects =====
    static int createdCount = 0;

    Counter() {
        value = 0;
        createdCount++; // every new Counter increases the shared count
    }

    // ===== INSTANCE method: needs an object (this.value) =====
    void increment() {
        value++;
    }

    void show() {
        System.out.println("this.value = " + value + " | shared createdCount = " + createdCount);
    }

    // ===== STATIC method: belongs to the CLASS, not one object =====
    // Can use static fields. Cannot use instance fields directly (no "this").
    static void showCreatedCount() {
        System.out.println("Counters created so far: " + createdCount);
        // System.out.println(value); // ERROR: no object here
    }
}

public class Lesson18_StaticVsInstance {
    public static void main(String[] args) {
        // main is static → JVM can call it without creating Lesson18_StaticVsInstance

        Counter.showCreatedCount(); // call static method via CLASS name

        Counter a = new Counter();
        Counter b = new Counter();

        a.increment();
        a.increment();
        b.increment();

        System.out.println("--- each object has its own value ---");
        a.show(); // value 2
        b.show(); // value 1
        // createdCount is 2 for BOTH (shared)

        System.out.println("--- static call ---");
        Counter.showCreatedCount();

        // You CAN call static via object, but DON'T — it's confusing
        // a.showCreatedCount(); // works, but style is bad

        System.out.println("--- Lesson 16 connection ---");
        // StudentRecord.fromFileLine(...) was static:
        //   no student exists yet → method CREATES one from text
        // s.toFileLine() was instance:
        //   needs THAT student's name/marks

        explainRules();
    }

    // static helper: no need for an object of this class
    static void explainRules() {
        System.out.println("Rule 1: static → ClassName.method()");
        System.out.println("Rule 2: instance → object.method()");
        System.out.println("Rule 3: static methods cannot use instance fields");
    }
}

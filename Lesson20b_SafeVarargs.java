// Lesson 20b: Heap pollution warning + @SafeVarargs
// Compile: javac Lesson20b_SafeVarargs.java
// Run:     java Lesson20b_SafeVarargs
//
// Place: after Lesson 20 (Generics).
//
// Heap pollution = at runtime, a generic typed spot holds the WRONG type
// (because generics are erased, and varargs becomes an Object[] under the hood).
//
// Compiler warns on generic varargs methods. @SafeVarargs says:
// "I promise I won't misuse the array" → warning silenced (use carefully).

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lesson20b_SafeVarargs {
    public static void main(String[] args) {
        System.out.println("=== 1) SAFE use of generic varargs ===");
        List<String> names = asListSafe("Asha", "Riya", "Dev");
        System.out.println(names);

        System.out.println();
        System.out.println("=== 2) Why the compiler warns (heap pollution risk) ===");
        // BAD pattern (do not copy): store the T... array and let it escape.
        // Demonstrated conceptually below with a raw/unsafe helper comment.
        demoWhyWarningExists();

        System.out.println();
        System.out.println("=== 3) @SafeVarargs — only when method is truly safe ===");
        printAll("one", "two", "three");
    }

    /**
     * SAFE: we only READ the varargs / copy into a new List.
     * We never store the T[] anywhere that outsiders can put wrong types into.
     *
     * Without @SafeVarargs, javac shows:
     *   warning: [unchecked] Possible heap pollution from parameterized vararg type
     */
    @SafeVarargs
    static <T> List<T> asListSafe(T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    /**
     * Also SAFE: only iterate / print. Don't expose the array.
     */
    @SafeVarargs
    static <T> void printAll(T... items) {
        for (T item : items) {
            System.out.println("  item = " + item);
        }
    }

    /**
     * Illustration of UNSAFE idea (heap pollution).
     * Varargs T... is compiled roughly like T[] which is really Object[] after erasure.
     * If that array escapes, someone can put a wrong type into it → ClassCastException later.
     */
    static void demoWhyWarningExists() {
        // Imagine an unsafe method like:
        //   static <T> T[] wrap(T... items) { return items; }  // array escapes!
        //
        // Then:
        //   Object[] arr = wrap("hi");  // sneaky upcast
        //   arr[0] = 123;               // puts Integer into "String[]" slot
        //   String s = (String) arr[0]; // boom — heap pollution became a crash
        //
        // That is why javac warns on generic varargs unless you mark @SafeVarargs
        // AND you really don't let the array escape / get written wrongly.

        System.out.println("Generics erase to Object at runtime.");
        System.out.println("T... becomes an array that can be polluted if it escapes.");
        System.out.println("Rule: with @SafeVarargs, only READ items; don't return/store the array.");
    }
}

// Lesson 23: Lambda + forEach (short functions you pass around)
// Compile: javac Lesson23_Lambda.java
// Run:     java Lesson23_Lambda

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Lesson23_Lambda {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Shashwat");
        names.add("Asha");
        names.add("Ravi");
        names.add("Neha");

        // ===== Old way: for-each loop =====
        System.out.println("--- for-each loop ---");
        for (String name : names) {
            System.out.println(name);
        }

        // ===== forEach + lambda =====
        // Lambda = short unnamed function
        // (parameters) -> body
        System.out.println("--- forEach + lambda ---");
        names.forEach((name) -> System.out.println(name));

        // Even shorter (method reference)
        System.out.println("--- method reference ---");
        names.forEach(System.out::println);

        // ===== Lambda with a bit of logic =====
        System.out.println("--- names starting with A/N ---");
        names.forEach((name) -> {
            if (name.startsWith("A") || name.startsWith("N")) {
                System.out.println(name);
            }
        });

        // ===== Runnable before vs after lambda =====
        System.out.println("--- Runnable ---");

        // Old anonymous class
        Runnable oldStyle = new Runnable() {
            @Override
            public void run() {
                System.out.println("Old style run");
            }
        };

        // Lambda version (same idea, less code)
        Runnable newStyle = () -> System.out.println("Lambda style run");

        oldStyle.run();
        newStyle.run();

        // ===== What forEach really expects =====
        // Consumer<T> = takes T, returns nothing
        Consumer<String> printer = (s) -> System.out.println("Hello, " + s);
        names.forEach(printer);

        // ===== Numbers example =====
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        System.out.println("--- squares ---");
        nums.forEach(n -> System.out.println(n + " squared = " + (n * n)));
    }
}

// Lesson 24b: Same tasks WITH stream and WITHOUT stream
// Compile: javac Lesson24b_StreamVsLoop.java
// Run:     java Lesson24b_StreamVsLoop

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lesson24b_StreamVsLoop {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(3, 8, 1, 10, 5, 12, 7);
        List<String> names = List.of("Shashwat", "Asha", "Ravi", "Neha", "Arjun");

        System.out.println("Original numbers: " + numbers);
        System.out.println("Original names:   " + names);
        System.out.println();

        // =========================================================
        // TASK 1: Keep only even numbers
        // =========================================================

        // WITH STREAM
        List<Integer> evensStream = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        // WITHOUT STREAM (normal loop)
        List<Integer> evensLoop = new ArrayList<>();
        for (int n : numbers) {
            if (n % 2 == 0) {
                evensLoop.add(n);
            }
        }

        System.out.println("TASK 1 — Evens");
        System.out.println("  stream: " + evensStream);
        System.out.println("  loop:   " + evensLoop);
        System.out.println();

        // =========================================================
        // TASK 2: Double every number
        // =========================================================

        // WITH STREAM
        List<Integer> doubledStream = numbers.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());

        // WITHOUT STREAM
        List<Integer> doubledLoop = new ArrayList<>();
        for (int n : numbers) {
            doubledLoop.add(n * 2);
        }

        System.out.println("TASK 2 — Doubled");
        System.out.println("  stream: " + doubledStream);
        System.out.println("  loop:   " + doubledLoop);
        System.out.println();

        // =========================================================
        // TASK 3: Even numbers, then square them
        // =========================================================

        // WITH STREAM
        List<Integer> evenSquaresStream = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .collect(Collectors.toList());

        // WITHOUT STREAM
        List<Integer> evenSquaresLoop = new ArrayList<>();
        for (int n : numbers) {
            if (n % 2 == 0) {
                evenSquaresLoop.add(n * n);
            }
        }

        System.out.println("TASK 3 — Even squares");
        System.out.println("  stream: " + evenSquaresStream);
        System.out.println("  loop:   " + evenSquaresLoop);
        System.out.println();

        // =========================================================
        // TASK 4: Names starting with "A", make UPPERCASE
        // =========================================================

        // WITH STREAM
        List<String> aNamesStream = names.stream()
                .filter(name -> name.startsWith("A"))
                .map(name -> name.toUpperCase())
                .collect(Collectors.toList());

        // WITHOUT STREAM
        List<String> aNamesLoop = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("A")) {
                aNamesLoop.add(name.toUpperCase());
            }
        }

        System.out.println("TASK 4 — A names uppercase");
        System.out.println("  stream: " + aNamesStream);
        System.out.println("  loop:   " + aNamesLoop);
        System.out.println();

        // =========================================================
        // TASK 5: Count numbers > 5
        // =========================================================

        // WITH STREAM
        long countStream = numbers.stream()
                .filter(n -> n > 5)
                .count();

        // WITHOUT STREAM
        int countLoop = 0;
        for (int n : numbers) {
            if (n > 5) {
                countLoop++;
            }
        }

        System.out.println("TASK 5 — Count > 5");
        System.out.println("  stream: " + countStream);
        System.out.println("  loop:   " + countLoop);
        System.out.println();

        // =========================================================
        // TASK 6: Sum of all numbers
        // =========================================================

        // WITH STREAM
        int sumStream = numbers.stream()
                .mapToInt(n -> n)
                .sum();

        // WITHOUT STREAM
        int sumLoop = 0;
        for (int n : numbers) {
            sumLoop += n;
        }

        System.out.println("TASK 6 — Sum");
        System.out.println("  stream: " + sumStream);
        System.out.println("  loop:   " + sumLoop);
    }
}

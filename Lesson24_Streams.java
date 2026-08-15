// Lesson 24: Streams (filter, map, collect)
// Compile: javac Lesson24_Streams.java
// Run:     java Lesson24_Streams

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lesson24_Streams {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(3, 8, 1, 10, 5, 12, 7);
        List<String> names = List.of("Shashwat", "Asha", "Ravi", "Neha", "Arjun");

        // ===== Pipeline idea =====
        // source -> filter/map/... -> collect/forEach (end)


        // 1) Keep even numbers
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("Evens: " + evens);

        // 2) Double every number
        List<Integer> doubled = numbers.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());
        System.out.println("Doubled: " + doubled);

        // 3) Chain: even numbers, then square them
        List<Integer> evenSquares = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .collect(Collectors.toList());
        System.out.println("Even squares: " + evenSquares);

        // 4) Names starting with A, uppercase
        List<String> aNames = names.stream()
                .filter(name -> name.startsWith("A"))
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("A names: " + aNames);

        // 5) count / sum style results
        long countOver5 = numbers.stream()
                .filter(n -> n > 5)
                .count();
        System.out.println("Count > 5: " + countOver5);

        int sum = numbers.stream()
                .mapToInt(n -> n) // IntStream for sum()
                .sum();
        System.out.println("Sum: " + sum);

        // 6) forEach at the end (no new list)
        System.out.print("Print odds: ");
        numbers.stream()
                .filter(n -> n % 2 != 0)
                .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // ===== Same task WITHOUT stream (for comparison) =====
        List<Integer> evensOld = new ArrayList<>();
        for (int n : numbers) {
            if (n % 2 == 0) {
                evensOld.add(n);
            }
        }
        System.out.println("Evens (old loop): " + evensOld);
    }
}

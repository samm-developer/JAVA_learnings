// Lesson 29: Sorting with Comparable & Comparator
// Compile: javac Lesson29_Sorting.java
// Run:     java Lesson29_Sorting

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lesson29_Sorting {
    public static void main(String[] args) {

        // ===== 1) Sort simple strings / numbers =====
        List<String> names = new ArrayList<>(List.of("Ravi", "Asha", "Neha", "Shashwat"));
        Collections.sort(names); // natural A-Z order
        System.out.println("Names A-Z: " + names);

        List<Integer> nums = new ArrayList<>(List.of(40, 10, 25, 5));
        nums.sort(null); // natural order (same idea as Collections.sort)
        System.out.println("Numbers:   " + nums);

        // ===== 2) Sort objects — natural order via Comparable =====
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Interstellar", 2014, 8.7));
        movies.add(new Movie("Inception", 2010, 8.8));
        movies.add(new Movie("Oppenheimer", 2023, 8.3));
        movies.add(new Movie("The Dark Knight", 2008, 9.0));

        Collections.sort(movies); // uses compareTo → by year
        System.out.println("--- by year (Comparable) ---");
        movies.forEach(System.out::println);

        // ===== 3) Sort by rating (Comparator + lambda) =====
        movies.sort((a, b) -> Double.compare(b.rating(), a.rating())); // high → low
        System.out.println("--- by rating high→low (Comparator) ---");
        movies.forEach(System.out::println);

        // ===== 4) Sort by title =====
        movies.sort(Comparator.comparing(Movie::title));
        System.out.println("--- by title ---");
        movies.forEach(System.out::println);

        // ===== 5) Then-comparing: year, then rating =====
        movies.sort(
                Comparator.comparingInt(Movie::year)
                        .thenComparing(Movie::rating, Comparator.reverseOrder())
        );
        System.out.println("--- by year, then rating ---");
        movies.forEach(System.out::println);
    }
}

// Comparable = this object knows its "natural" order
record Movie(String title, int year, double rating) implements Comparable<Movie> {
    @Override
    public int compareTo(Movie other) {
        return Integer.compare(this.year, other.year); // older first
    }

    @Override
    public String toString() {
        return year + " | " + rating + " | " + title;
    }
}

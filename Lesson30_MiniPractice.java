// Lesson 30 Mini Practice: Optional city lookup
// Compile: javac Lesson30_MiniPractice.java
// Run:     java Lesson30_MiniPractice

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Lesson30_MiniPractice {
    public static void main(String[] args) {
        Map<String, String> cities = new HashMap<>();
        cities.put("Shashwat", "London");
        cities.put("Asha", "Delhi");
        cities.put("Neha", "Mumbai");
        // Ravi is missing on purpose

        String[] friends = {"Asha", "Ravi", "Neha", "Shashwat"};

        for (String friend : friends) {
            String city = findCity(cities, friend).orElse("Unknown");
            System.out.println(friend + " => " + city);
        }
    }

    static Optional<String> findCity(Map<String, String> map, String friend) {
        return Optional.ofNullable(map.get(friend));
    }
}

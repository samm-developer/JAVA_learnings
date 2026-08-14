// Lesson 13: Collections (ArrayList & HashMap)
// Compile: javac Lesson13_Collections.java
// Run:     java Lesson13_Collections

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Lesson13_Collections {
    public static void main(String[] args) {

        // ===== ArrayList: growable list =====
        // Unlike arrays, size is NOT fixed
        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Banana"); // duplicates allowed

        System.out.println("--- ArrayList ---");
        System.out.println("Fruits: " + fruits);
        System.out.println("Size: " + fruits.size());
        System.out.println("First: " + fruits.get(0));

        fruits.set(1, "Orange");          // replace index 1
        fruits.remove("Mango");           // remove by value
        System.out.println("After edit: " + fruits);
        System.out.println("Contains Apple? " + fruits.contains("Apple"));

        System.out.println("Loop:");
        for (String fruit : fruits) {
            System.out.println(" - " + fruit);
        }

        // ===== HashMap: key → value =====
        HashMap<String, Integer> marks = new HashMap<>();
        marks.put("Shashwat", 98);
        marks.put("Asha", 89);
        marks.put("Ravi", 75);
        marks.put("Asha", 92); // same key overwrites old value

        System.out.println("--- HashMap ---");
        System.out.println("Marks: " + marks);
        System.out.println("Asha: " + marks.get("Asha"));
        System.out.println("Has Ravi? " + marks.containsKey("Ravi"));

        marks.remove("Ravi");
        System.out.println("After remove: " + marks);

        System.out.println("Loop keys + values:");
        for (String name : marks.keySet()) {
            System.out.println(name + " scored " + marks.get(name));
        }

        // ===== HashSet: unique values only =====
        HashSet<String> tags = new HashSet<>();
        tags.add("java");
        tags.add("oop");
        tags.add("java"); // duplicate ignored

        System.out.println("--- HashSet ---");
        System.out.println("Tags: " + tags);

        // ===== Practical: student roster =====
        ArrayList<String> names = new ArrayList<>();
        names.add("Shashwat");
        names.add("Asha");
        names.add("Ravi");

        HashMap<String, Integer> roster = new HashMap<>();
        for (String name : names) {
            roster.put(name, name.length() * 10); // fake score
        }
        System.out.println("--- roster ---");
        System.out.println(roster);
    }
}

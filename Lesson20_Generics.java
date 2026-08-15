// Lesson 20: Generics (why ArrayList<String> has <String>)
// Compile: javac Lesson20_Generics.java
// Run:     java Lesson20_Generics

import java.util.ArrayList;

public class Lesson20_Generics {
    public static void main(String[] args) {

        // ===== Without generics (old style — don't write this) =====
        // ArrayList raw = new ArrayList();
        // raw.add("Hello");
        // raw.add(100); // allowed — dangerous mix
        // String s = (String) raw.get(0); // must cast; easy to crash

        // ===== With generics: ArrayList<TYPE> =====
        ArrayList<String> names = new ArrayList<>();
        names.add("Shashwat");
        names.add("Asha");
        // names.add(100); // COMPILE ERROR — only String allowed

        String first = names.get(0); // no cast needed
        System.out.println("First name: " + first);

        ArrayList<Integer> scores = new ArrayList<>();
        scores.add(88);
        scores.add(92);
        // scores.add("hi"); // COMPILE ERROR

        int total = 0;
        for (int score : scores) {
            total += score;
        }
        System.out.println("Total score: " + total);

        // ===== Your own generic box =====
        Box<String> stringBox = new Box<>();
        stringBox.setItem("Java");
        System.out.println("stringBox: " + stringBox.getItem());

        Box<Integer> intBox = new Box<>();
        intBox.setItem(42);
        System.out.println("intBox: " + intBox.getItem());

        // ===== Generic method =====
        System.out.println("First of names: " + firstOf(names));
        System.out.println("First of scores: " + firstOf(scores));

        // ===== Why Integer, not int? =====
        // Generics need object types, not primitives.
        // int → Integer, double → Double, boolean → Boolean (autoboxing)
        Integer n = 10;      // int automatically becomes Integer
        int plain = n;       // Integer automatically becomes int
        System.out.println("autoboxing demo: " + plain);
    }

    // <T> means: this method works for any type T
    public static <T> T firstOf(ArrayList<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}

// T is a placeholder type — chosen when you create the object
class Box<T> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}

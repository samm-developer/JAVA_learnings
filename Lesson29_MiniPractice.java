// Lesson 29 Mini Practice: sort students by marks and by name
// Compile: javac Lesson29_MiniPractice.java
// Run:     java Lesson29_MiniPractice

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Lesson29_MiniPractice {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Ravi", 75));
        students.add(new Student("Asha", 92));
        students.add(new Student("Neha", 88));
        students.add(new Student("Shashwat", 92));

        System.out.println("--- original ---");
        students.forEach(System.out::println);

        // 1) Sort by marks: high → low
        students.sort((a, b) -> Integer.compare(b.marks(), a.marks()));
        // same idea:
        // students.sort(Comparator.comparingInt(Student::marks).reversed());

        System.out.println("--- by marks high → low ---");
        students.forEach(System.out::println);

        // 2) Sort by name: A → Z
        students.sort(Comparator.comparing(Student::name));

        System.out.println("--- by name A → Z ---");
        students.forEach(System.out::println);

        // Bonus: marks high→low, if tie then name A→Z
        students.sort(
                Comparator.comparingInt(Student::marks).reversed()
                        .thenComparing(Student::name)
        );
        System.out.println("--- marks high→low, then name ---");
        students.forEach(System.out::println);
    }
}

record Student(String name, int marks) {
    @Override
    public String toString() {
        return name + " | " + marks;
    }
}

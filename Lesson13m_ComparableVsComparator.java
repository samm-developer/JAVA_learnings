// Lesson 13m: Comparable vs Comparator (for collections)
// Compile: javac LessonConsole.java Lesson13m_ComparableVsComparator.java
// Run:     java Lesson13m_ComparableVsComparator
//
// After: Lesson 13l | Full sorting demo → Lesson 29_Sorting

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Lesson13m_ComparableVsComparator {

    public static void main(String[] args) {
        definitions();
        comparableTreeSet();
        comparatorCustomOrder();
        problemMissingComparable();
        inMapsAndQueues();
        summary();
    }

    static void definitions() {
        LessonConsole.heading("=== 0) Comparable vs Comparator ===");
        System.out.println("""
                Comparable (inside class)     compareTo(other)     natural order
                Comparator (outside/lambda)   compare(a,b)         custom order

                TreeSet / TreeMap keys need ordering from one of these.
                """);
    }

    static void comparableTreeSet() {
        LessonConsole.heading("=== 1) Comparable — natural order in class ===");
        record Student(String name, int score) implements Comparable<Student> {
            public int compareTo(Student o) {
                return Integer.compare(this.score, o.score); // low score first
            }
            public String toString() { return name + "(" + score + ")"; }
        }

        Set<Student> byScore = new TreeSet<>();
        byScore.add(new Student("Asha", 92));
        byScore.add(new Student("Ravi", 75));
        byScore.add(new Student("Neha", 88));
        System.out.println("  TreeSet natural (score asc): " + byScore);
        System.out.println();
    }

    static void comparatorCustomOrder() {
        LessonConsole.heading("=== 2) Comparator — custom order without changing class ===");
        record Student(String name, int score) {
            public String toString() { return name + "(" + score + ")"; }
        }

        Comparator<Student> byName = Comparator.comparing(Student::name);
        Set<Student> byNameSet = new TreeSet<>(byName);
        byNameSet.add(new Student("Ravi", 75));
        byNameSet.add(new Student("Asha", 92));
        System.out.println("  TreeSet Comparator (name): " + byNameSet);

        Comparator<Student> byScoreDesc = Comparator.comparingInt(Student::score).reversed();
        TreeSet<Student> top = new TreeSet<>(byScoreDesc);
        top.add(new Student("Ravi", 75));
        top.add(new Student("Asha", 92));
        System.out.println("  TreeSet high score first: " + top);
        System.out.println();
    }

    static void problemMissingComparable() {
        LessonConsole.heading("=== PROBLEM: TreeSet without Comparable or Comparator ===");
        class BadKey {
            String id;
            BadKey(String id) { this.id = id; }
        }
        try {
            Set<BadKey> set = new TreeSet<>();
            set.add(new BadKey("A"));
            set.add(new BadKey("B"));
        } catch (Exception e) {
            System.out.println("  ❌ " + e.getClass().getSimpleName()
                    + " — TreeSet can't compare BadKey");
        }
        System.out.println("  Fix: implement Comparable OR new TreeSet<>(comparator)");
        System.out.println();
    }

    static void inMapsAndQueues() {
        LessonConsole.heading("=== 3) Used in TreeMap & PriorityQueue ===");
        TreeMap<String, Integer> map = new TreeMap<>(); // String natural order
        map.put("zebra", 1);
        map.put("apple", 2);
        System.out.println("  TreeMap keys sorted: " + map.keySet());

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(3);
        maxHeap.offer(1);
        System.out.println("  PriorityQueue Comparator.reverseOrder poll=" + maxHeap.poll());
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Comparable vs Comparator ===");
        System.out.println("""
                Comparable  → one natural order baked into class (compareTo)
                Comparator  → many possible orders (lambda, method ref, reversed)
                TreeSet/TreeMap/PriorityQueue all depend on comparison
                Sorting lists → also Lesson 29_Sorting
                Next:         Lesson 13n SortedMap & NavigableMap
                """);
    }
}

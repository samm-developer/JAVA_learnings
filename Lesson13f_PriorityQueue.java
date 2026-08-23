// Lesson 13f: PriorityQueue — min-heap + internal working
// Compile: javac LessonConsole.java Lesson13f_PriorityQueue.java
// Run:     java Lesson13f_PriorityQueue
//
// After: Lesson 13e (ConcurrentHashMap)
//
// ========== CHEAT SHEET ==========
// Internal: binary min-heap stored in Object[] queue
//           index 0 = smallest element
//           parent(i)=(i-1)/2   left=2i+1   right=2i+2
// offer/poll: O(log n) — bubble up / sift down
// peek:       O(1)
// Iterator:   NOT sorted order (heap array order) — use poll() for sorted removal
// Use when:    always process smallest/highest priority item next (tasks, Dijkstra, merge K lists)

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Lesson13f_PriorityQueue {

    public static void main(String[] args) {
        internalPicture();
        problemArrayListSort();
        solutionMinHeap();
        maxHeapCustomComparator();
        iteratorVsPoll();
        taskSchedulerDemo();
        summary();
    }

    static void internalPicture() {
        LessonConsole.heading("=== 0) Internal shape (binary heap in array) ===");
        System.out.println("""
                PriorityQueue stores heap in array `queue[]`:

                  indices:  0   1   2   3   4
                  values:  [ 1,  3,  2,  7,  5 ]   (min-heap: parent <= children)

                         1
                       /   \\
                      3     2
                     / \\
                    7   5

                offer(x): add at end → bubble UP until heap valid
                poll():   swap root with last → bubble DOWN root
                """);
    }

    static void problemArrayListSort() {
        LessonConsole.heading("=== PROBLEM: sort whole list every time you need the minimum ===");
        int[] tasks = { 5, 1, 9, 3 };
        Arrays.sort(tasks); // O(n log n) to get one minimum
        System.out.println("  tasks=" + Arrays.toString(tasks));
        System.out.println("  next=" + tasks[0] + " but resort or shift if tasks keep arriving ❌");
        System.out.println();
    }

    static void solutionMinHeap() {
        LessonConsole.heading("=== SOLUTION: PriorityQueue (min-heap) ===");
        Queue<Integer> pq = new PriorityQueue<>();
        pq.offer(5);
        pq.offer(1);
        pq.offer(9);
        pq.offer(3);
        System.out.println("  offered: 5,1,9,3");
        System.out.println("  peek (min)=" + pq.peek() + "  O(1)");
        System.out.print("  poll order: ");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");
        }
        System.out.println("  ✅ always smallest first, O(log n) per poll");
        System.out.println();
    }

    static void maxHeapCustomComparator() {
        LessonConsole.heading("=== 2) Max-heap via Comparator ===");
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(1);
        maxHeap.offer(9);
        System.out.println("  max poll first: " + maxHeap.poll() + " (expected 9)");
        System.out.println();
    }

    static void iteratorVsPoll() {
        LessonConsole.heading("=== 3) Iterator order ≠ priority order ===");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(10);
        pq.add(1);
        pq.add(5);
        System.out.println("  iterate (heap array layout): " + pq + "  ❌ not sorted");
        System.out.print("  poll order (true priority):  ");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");
        }
        System.out.println("  ✅");
        System.out.println();
    }

    static void taskSchedulerDemo() {
        LessonConsole.heading("=== 4) Demo: process tasks by priority ===");
        record Task(String name, int priority) { }

        PriorityQueue<Task> jobs = new PriorityQueue<>(Comparator.comparingInt(Task::priority));
        jobs.offer(new Task("email", 3));
        jobs.offer(new Task("payment", 1));
        jobs.offer(new Task("report", 2));

        System.out.print("  process order: ");
        while (!jobs.isEmpty()) {
            Task t = jobs.poll();
            System.out.print(t.name() + "(p" + t.priority() + ") ");
        }
        System.out.println();
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: PriorityQueue ===");
        System.out.println("""
                Implements:   Queue — use offer/poll/peek (not index access)
                Internal:     array-based binary heap
                Min default:  new PriorityQueue<>()
                Max:          new PriorityQueue<>(Comparator.reverseOrder())
                Not thread-safe → PriorityBlockingQueue for concurrent case
                Next:         Lesson 13g TreeMap & more collections
                """);
    }
}

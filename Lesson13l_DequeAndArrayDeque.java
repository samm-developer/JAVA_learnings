// Lesson 13l: Deque & ArrayDeque — internal working
// Compile: javac LessonConsole.java Lesson13l_DequeAndArrayDeque.java
// Run:     java Lesson13l_DequeAndArrayDeque
//
// After: Lesson 13k | PriorityQueue → 13f

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class Lesson13l_DequeAndArrayDeque {

    public static void main(String[] args) {
        dequeInterface();
        internalRingBuffer();
        queueAndStack();
        problemLinkedListAsDeque();
        summary();
    }

    static void dequeInterface() {
        LessonConsole.heading("=== 0) Deque — double-ended queue ===");
        System.out.println("""
                Deque = insert/remove at BOTH ends

                Queue side:  offerLast / pollFirst   (FIFO)
                Stack side:  push / pop               (LIFO)

                Also: offerFirst, pollLast, peekFirst, peekLast
                """);
    }

    static void internalRingBuffer() {
        LessonConsole.heading("=== 1) ArrayDeque internal — circular array ===");
        System.out.println("""
                ArrayDeque stores elements in Object[] elements
                  head index → front
                  tail index → back (wraps around — ring buffer)

                offerLast  → write at tail, advance tail (wrap at end)
                pollFirst  → read at head, advance head

                No Node objects → faster & less memory than LinkedList
                """);
        Deque<Integer> d = new ArrayDeque<>();
        d.offerLast(1);
        d.offerLast(2);
        d.offerFirst(0);
        System.out.println("  offerFirst(0), offerLast(1,2) → " + d);
        System.out.println();
    }

    static void queueAndStack() {
        LessonConsole.heading("=== 2) FIFO queue vs LIFO stack ===");
        Deque<String> queue = new ArrayDeque<>();
        queue.offerLast("A");
        queue.offerLast("B");
        queue.offerLast("C");
        System.out.print("  FIFO pollFirst: ");
        while (!queue.isEmpty()) {
            System.out.print(queue.pollFirst() + " ");
        }
        System.out.println();

        Deque<String> stack = new ArrayDeque<>();
        stack.push("X");
        stack.push("Y");
        System.out.println("  LIFO pop: " + stack.pop() + " then " + stack.pop());
        System.out.println();
    }

    static void problemLinkedListAsDeque() {
        LessonConsole.heading("=== PROBLEM: LinkedList as Deque — extra Node objects ===");
        System.out.println("""
                LinkedList implements Deque but each element = Node(prev, item, next)
                  → more heap objects, worse cache locality
                """);
        LinkedList<String> ll = new LinkedList<>();
        ll.addLast("only-if-you-need-LL-features");
        System.out.println("  LinkedList: " + ll + "  — prefer ArrayDeque for queue/stack");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Deque / ArrayDeque ===");
        System.out.println("""
                Deque           → interface (both-end ops)
                ArrayDeque      → ring buffer array — default for queue/stack
                PriorityQueue   → by priority, not FIFO (13f)
                Not thread-safe → LinkedBlockingDeque / ArrayBlockingQueue
                Next:           Lesson 13m Comparable vs Comparator
                """);
    }
}

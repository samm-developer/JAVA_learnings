// Lesson 36i: Builder pattern (Creational)
// Compile: javac Lesson36i_Builder.java
// Run:     java Lesson36i_Builder
//
// After: Lesson 36h (Factory)
//
// ========== CHEAT SHEET ==========
// Problem:  constructor with many params — easy to swap order, hard to read optional fields
// Solution: fluent Builder — set fields step-by-step, validate in build()
// Java:     HttpClient.newBuilder(), StringBuilder, Lombok @Builder

import java.util.ArrayList;
import java.util.List;

public class Lesson36i_Builder {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        System.out.println("=== PROBLEM: telescoping constructor ===");
        // Which boolean? Which tag order? Hard to extend.
        BadTask t = new BadTask("Learn patterns", false, List.of("java", "design"));
        System.out.println("  " + t);
        System.out.println("  new BadTask(title, done, tags) — unclear at call site ❌");
        System.out.println();
    }

    static void solution() {
        System.out.println("=== SOLUTION: Builder — step-by-step, readable ===");
        GoodTask t = new GoodTask.Builder()
                .title("Learn patterns")
                .done(false)
                .tag("java")
                .tag("design")
                .build();
        System.out.println("  " + t + "  ✅");
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: Builder ===");
        System.out.println("""
                When:     object has many optional fields or validation rules
                How:      private ctor + static Builder with chained setters + build()
                Benefit:  readable call site; invalid objects rejected in build()
                Next:     Lesson 36j Adapter
                """);
    }

    // --- PROBLEM ---
    static class BadTask {
        final String title;
        final boolean done;
        final List<String> tags;

        BadTask(String title, boolean done, List<String> tags) {
            this.title = title;
            this.done = done;
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "BadTask{title='" + title + "', done=" + done + ", tags=" + tags + "}";
        }
    }

    // --- SOLUTION ---
    static class GoodTask {
        final String title;
        final boolean done;
        final List<String> tags;

        private GoodTask(Builder b) {
            this.title = b.title;
            this.done = b.done;
            this.tags = List.copyOf(b.tags);
        }

        static class Builder {
            private String title;
            private boolean done;
            private final List<String> tags = new ArrayList<>();

            Builder title(String title) {
                this.title = title;
                return this;
            }

            Builder done(boolean done) {
                this.done = done;
                return this;
            }

            Builder tag(String tag) {
                this.tags.add(tag);
                return this;
            }

            GoodTask build() {
                if (title == null || title.isBlank()) {
                    throw new IllegalStateException("title required");
                }
                return new GoodTask(this);
            }
        }

        @Override
        public String toString() {
            return "GoodTask{title='" + title + "', done=" + done + ", tags=" + tags + "}";
        }
    }
}

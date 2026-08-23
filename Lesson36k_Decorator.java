// Lesson 36k: Decorator pattern (Structural)
// Compile: javac LessonConsole.java Lesson36k_Decorator.java
// Run:     java Lesson36k_Decorator
//
// After: Lesson 36j (Adapter)
//
// ========== CHEAT SHEET ==========
// Problem:  subclass explosion — Coffee, Coffee+Milk, Coffee+Milk+Log, ...
// Solution: wrap object in decorator(s) that share same interface, delegate + add behavior
// Spring:   AOP proxies add logging/security/tx around beans (decorator-like)

public class Lesson36k_Decorator {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        LessonConsole.heading("=== PROBLEM: one subclass per combination ===");
        Coffee plain = new PlainCoffee();
        Coffee withMilk = new CoffeeWithMilk();
        Coffee withMilkAndLog = new CoffeeWithMilkAndLog();
        System.out.println("  " + plain.describe() + " cost=" + plain.cost());
        System.out.println("  " + withMilk.describe() + " cost=" + withMilk.cost());
        System.out.println("  " + withMilkAndLog.describe() + " cost=" + withMilkAndLog.cost());
        System.out.println("  Add Sugar? → new CoffeeWithMilkAndSugar, CoffeeWithSugarAndLog, ... ❌");
        System.out.println();
    }

    static void solution() {
        LessonConsole.heading("=== SOLUTION: Decorator — stack wrappers at runtime ===");
        Coffee coffee = new SimpleCoffee();
        coffee = new MilkDecorator(coffee);
        coffee = new LoggingDecorator(coffee);
        System.out.println("  " + coffee.describe() + " cost=" + coffee.cost() + "  ✅");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Decorator ===");
        System.out.println("""
                When:     add features without editing original class or exploding subclasses
                How:      decorator implements same interface, holds inner object, delegates + extras
                Compose:  new LoggingDecorator(new MilkDecorator(new SimpleCoffee()))
                Next:     Lesson 36l Observer
                """);
    }

    interface Coffee {
        String describe();
        int cost();
    }

    // --- PROBLEM: fixed subclass combos ---
    static class PlainCoffee implements Coffee {
        public String describe() { return "Coffee"; }
        public int cost() { return 100; }
    }

    static class CoffeeWithMilk implements Coffee {
        public String describe() { return "Coffee + Milk"; }
        public int cost() { return 130; }
    }

    static class CoffeeWithMilkAndLog implements Coffee {
        public String describe() {
            System.out.println("  [LOG] serving: Coffee + Milk");
            return "Coffee + Milk";
        }
        public int cost() { return 130; }
    }

    // --- SOLUTION: composable decorators ---
    static class SimpleCoffee implements Coffee {
        public String describe() { return "Coffee"; }
        public int cost() { return 100; }
    }

    static class MilkDecorator implements Coffee {
        private final Coffee inner;

        MilkDecorator(Coffee inner) {
            this.inner = inner;
        }

        public String describe() {
            return inner.describe() + " + Milk";
        }

        public int cost() {
            return inner.cost() + 30;
        }
    }

    static class LoggingDecorator implements Coffee {
        private final Coffee inner;

        LoggingDecorator(Coffee inner) {
            this.inner = inner;
        }

        public String describe() {
            String d = inner.describe();
            System.out.println("  [LOG] serving: " + d);
            return d;
        }

        public int cost() {
            return inner.cost();
        }
    }
}

// Lesson 36g: Strategy pattern (Behavioral)
// Compile: javac LessonConsole.java Lesson36g_Strategy.java
// Run:     java Lesson36g_Strategy
//
// After: Lesson 36f (Singleton)
//
// ========== CHEAT SHEET ==========
// Problem:  growing if/else (or switch) for each payment type — edit Checkout every time
// Solution: PayStrategy interface; Checkout delegates to pluggable strategy
// Spring:   inject different PasswordEncoder / AuthProvider implementations

public class Lesson36g_Strategy {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        LessonConsole.heading("=== PROBLEM: if/else payment logic inside Checkout ===");
        BadCheckout cart = new BadCheckout();
        System.out.println("  " + cart.checkout("card", 500));
        System.out.println("  " + cart.checkout("upi", 500));
        System.out.println("  Adding NETBANKING? → edit BadCheckout.checkout() again ❌");
        System.out.println();
    }

    static void solution() {
        LessonConsole.heading("=== SOLUTION: Strategy — swap algorithm at runtime ===");
        GoodCheckout cart = new GoodCheckout(new CardPay());
        System.out.println("  " + cart.checkout(500));
        cart.setStrategy(new UpiPay());
        System.out.println("  " + cart.checkout(500));
        cart.setStrategy(new NetBankPay());
        System.out.println("  " + cart.checkout(500) + "  ✅ new type = new class, no edit to Checkout");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Strategy ===");
        System.out.println("""
                When:     several algorithms for the same job (pay, sort, compress)
                How:      interface + multiple impls; context holds current strategy
                vs if/else: open for extension (new class), closed for modification (context)
                Next:     Lesson 36h Factory
                """);
    }

    // --- PROBLEM ---
    static class BadCheckout {
        String checkout(String type, int amount) {
            if ("card".equalsIgnoreCase(type)) {
                return "Paid " + amount + " by CARD";
            } else if ("upi".equalsIgnoreCase(type)) {
                return "Paid " + amount + " by UPI";
            }
            throw new IllegalArgumentException("Unknown: " + type);
        }
    }

    // --- SOLUTION ---
    interface PayStrategy {
        String pay(int amount);
    }

    static class CardPay implements PayStrategy {
        public String pay(int amount) {
            return "Paid " + amount + " by CARD";
        }
    }

    static class UpiPay implements PayStrategy {
        public String pay(int amount) {
            return "Paid " + amount + " by UPI";
        }
    }

    static class NetBankPay implements PayStrategy {
        public String pay(int amount) {
            return "Paid " + amount + " by NETBANKING";
        }
    }

    static class GoodCheckout {
        private PayStrategy strategy;

        GoodCheckout(PayStrategy strategy) {
            this.strategy = strategy;
        }

        void setStrategy(PayStrategy strategy) {
            this.strategy = strategy;
        }

        String checkout(int amount) {
            return strategy.pay(amount);
        }
    }
}

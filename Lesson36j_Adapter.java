// Lesson 36j: Adapter pattern (Structural)
// Compile: javac Lesson36j_Adapter.java
// Run:     java Lesson36j_Adapter
//
// After: Lesson 36i (Builder)
//
// ========== CHEAT SHEET ==========
// Problem:  new code expects PaymentGateway.charge(); legacy vendor has payLegacy()
// Solution: adapter implements expected interface, delegates to legacy inside
// Real life: wrap old REST/XML SDK behind your clean interface

public class Lesson36j_Adapter {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        System.out.println("=== PROBLEM: client tied to legacy API ===");
        LegacyPaySystem legacy = new LegacyPaySystem();
        // Checkout expects charge() — legacy only has payLegacy()
        System.out.println("  legacy.payLegacy(500) = " + legacy.payLegacy(500));
        System.out.println("  Cannot pass LegacyPaySystem where PaymentGateway is required ❌");
        System.out.println();
    }

    static void solution() {
        System.out.println("=== SOLUTION: Adapter wraps legacy behind PaymentGateway ===");
        PaymentGateway gw = new LegacyPaymentAdapter(new LegacyPaySystem());
        System.out.println("  checkout.charge(500) = " + checkout(gw, 500) + "  ✅");
        System.out.println();
    }

    static String checkout(PaymentGateway gw, int amount) {
        return gw.charge(amount);
    }

    static void summary() {
        System.out.println("=== Summary: Adapter ===");
        System.out.println("""
                When:     existing class API does not match what callers need
                How:      adapter implements target interface, holds legacy object, translates calls
                vs Facade: adapter = one old class → new interface; facade = many classes → one simple API
                Next:     Lesson 36k Decorator
                """);
    }

    // --- legacy (cannot change) ---
    static class LegacyPaySystem {
        String payLegacy(int rupees) {
            return "Legacy paid " + rupees;
        }
    }

    // --- target interface your app uses ---
    interface PaymentGateway {
        String charge(int amount);
    }

    // --- SOLUTION: adapter ---
    static class LegacyPaymentAdapter implements PaymentGateway {
        private final LegacyPaySystem legacy;

        LegacyPaymentAdapter(LegacyPaySystem legacy) {
            this.legacy = legacy;
        }

        public String charge(int amount) {
            return legacy.payLegacy(amount);
        }
    }
}

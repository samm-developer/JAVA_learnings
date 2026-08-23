// Lesson 36h: Factory pattern (Creational)
// Compile: javac Lesson36h_Factory.java
// Run:     java Lesson36h_Factory
//
// After: Lesson 36g (Strategy)
//
// ========== CHEAT SHEET ==========
// Problem:  `new EmailNotifier()` / `new SmsNotifier()` scattered — add Push → change many files
// Solution: NotifierFactory.create(type) — one place decides which class to instantiate
// Spring:   BeanFactory / @Bean methods are factories

public class Lesson36h_Factory {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        System.out.println("=== PROBLEM: client picks concrete class everywhere ===");
        BadAlertService alerts = new BadAlertService();
        System.out.println("  " + alerts.sendWelcome("email"));
        System.out.println("  " + alerts.sendWelcome("sms"));
        System.out.println("  Add push alerts? → edit BadAlertService in multiple places ❌");
        System.out.println();
    }

    static void solution() {
        System.out.println("=== SOLUTION: Factory — central creation by type ===");
        GoodAlertService alerts = new GoodAlertService();
        System.out.println("  " + alerts.sendWelcome("email"));
        System.out.println("  " + alerts.sendWelcome("sms"));
        System.out.println("  " + alerts.sendWelcome("push") + "  ✅ new channel = factory + one new class");
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: Factory ===");
        System.out.println("""
                When:     object type chosen at runtime from a fixed set
                How:      factory method/class maps key → new XxxImpl()
                Benefit:  callers depend on Notifier interface, not concrete classes
                Next:     Lesson 36i Builder
                """);
    }

    // --- PROBLEM: creation logic duplicated / tied to concrete types ---
    static class BadAlertService {
        String sendWelcome(String channel) {
            Notifier notifier;
            if ("email".equalsIgnoreCase(channel)) {
                notifier = new EmailNotifier();
            } else if ("sms".equalsIgnoreCase(channel)) {
                notifier = new SmsNotifier();
            } else {
                throw new IllegalArgumentException("Unknown: " + channel);
            }
            return notifier.send("Welcome");
        }
    }

    // --- SOLUTION ---
    interface Notifier {
        String send(String msg);
    }

    static class EmailNotifier implements Notifier {
        public String send(String msg) {
            return "EMAIL: " + msg;
        }
    }

    static class SmsNotifier implements Notifier {
        public String send(String msg) {
            return "SMS: " + msg;
        }
    }

    static class PushNotifier implements Notifier {
        public String send(String msg) {
            return "PUSH: " + msg;
        }
    }

    static class NotifierFactory {
        static Notifier create(String type) {
            return switch (type.toLowerCase()) {
                case "email" -> new EmailNotifier();
                case "sms" -> new SmsNotifier();
                case "push" -> new PushNotifier();
                default -> throw new IllegalArgumentException("Unknown: " + type);
            };
        }
    }

    static class GoodAlertService {
        String sendWelcome(String channel) {
            Notifier notifier = NotifierFactory.create(channel);
            return notifier.send("Welcome");
        }
    }
}

// Lesson 36b: Design patterns (Factory, Strategy, Builder) — practical mini demos
// Compile: javac Lesson36b_DesignPatterns.java
// Run:     java Lesson36b_DesignPatterns
//
// Place: after core + HTTP client lessons, before / beside Spring Boot.
// Spring itself is heavy on these ideas (Factory beans, Strategy auth, Builders).

import java.util.ArrayList;
import java.util.List;

// ========== 1) STRATEGY — swap algorithm at runtime ==========
interface PayStrategy {
    String pay(int amount);
}

class CardPay implements PayStrategy {
    public String pay(int amount) {
        return "Paid " + amount + " by CARD";
    }
}

class UpiPay implements PayStrategy {
    public String pay(int amount) {
        return "Paid " + amount + " by UPI";
    }
}

class Checkout {
    private PayStrategy strategy;

    Checkout(PayStrategy strategy) {
        this.strategy = strategy;
    }

    void setStrategy(PayStrategy strategy) {
        this.strategy = strategy;
    }

    String checkout(int amount) {
        return strategy.pay(amount);
    }
}

// ========== 2) FACTORY — central place to create objects ==========
interface Notifier {
    String send(String msg);
}

class EmailNotifier implements Notifier {
    public String send(String msg) {
        return "EMAIL: " + msg;
    }
}

class SmsNotifier implements Notifier {
    public String send(String msg) {
        return "SMS: " + msg;
    }
}

class NotifierFactory {
    static Notifier create(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotifier();
            case "sms" -> new SmsNotifier();
            default -> throw new IllegalArgumentException("Unknown: " + type);
        };
    }
}

// ========== 3) BUILDER — build complex object step by step ==========
class Task36b {
    final String title;
    final boolean done;
    final List<String> tags;

    private Task36b(Builder b) {
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

        Task36b build() {
            if (title == null || title.isBlank()) {
                throw new IllegalStateException("title required");
            }
            return new Task36b(this);
        }
    }

    @Override
    public String toString() {
        return "Task36b{title='" + title + "', done=" + done + ", tags=" + tags + "}";
    }
}

public class Lesson36b_DesignPatterns {
    public static void main(String[] args) {
        System.out.println("=== Strategy ===");
        Checkout cart = new Checkout(new CardPay());
        System.out.println(cart.checkout(500));
        cart.setStrategy(new UpiPay());
        System.out.println(cart.checkout(500));

        System.out.println();
        System.out.println("=== Factory ===");
        Notifier n1 = NotifierFactory.create("email");
        Notifier n2 = NotifierFactory.create("sms");
        System.out.println(n1.send("Welcome"));
        System.out.println(n2.send("OTP 1234"));

        System.out.println();
        System.out.println("=== Builder ===");
        Task36b task = new Task36b.Builder()
                .title("Learn patterns")
                .done(false)
                .tag("java")
                .tag("design")
                .build();
        System.out.println(task);
    }
}

// Lesson 36l: Observer pattern (Behavioral)
// Compile: javac Lesson36l_Observer.java
// Run:     java Lesson36l_Observer
//
// After: Lesson 36k (Decorator)
//
// ========== CHEAT SHEET ==========
// Problem:  Order.setStatus() manually calls email(), sms(), push() — new channel = edit Order
// Solution: subject keeps observer list; on change, notify all registered listeners
// Spring:   ApplicationEventPublisher + @EventListener

import java.util.ArrayList;
import java.util.List;

public class Lesson36l_Observer {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        System.out.println("=== PROBLEM: Order knows every notification channel ===");
        BadOrder order = new BadOrder("ORD-99");
        order.setStatus("SHIPPED");
        System.out.println("  Add WhatsApp? → edit BadOrder.setStatus() again ❌");
        System.out.println();
    }

    static void solution() {
        System.out.println("=== SOLUTION: Observer — register listeners, subject notifies ===");
        GoodOrder order = new GoodOrder("ORD-99");
        order.addObserver(msg -> System.out.println("  Email: " + msg));
        order.addObserver(msg -> System.out.println("  SMS:   " + msg));
        order.addObserver(msg -> System.out.println("  Push:  " + msg));
        order.setStatus("SHIPPED");
        System.out.println("  ✅ new channel = new observer, Order unchanged");
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: Observer ===");
        System.out.println("""
                When:     one state change must reach many unrelated listeners
                How:      subject maintains List<Observer>; setState → loop notify
                Benefit:  subject and observers stay loosely coupled
                Next:     Lesson 36m Template Method
                """);
    }

    // --- PROBLEM: hard-coded channels inside subject ---
    static class BadOrder {
        private final String id;
        private String status;

        BadOrder(String id) {
            this.id = id;
            this.status = "CREATED";
        }

        void setStatus(String status) {
            this.status = status;
            String msg = id + " is now " + status;
            sendEmail(msg);
            sendSms(msg);
        }

        void sendEmail(String msg) {
            System.out.println("  Email: " + msg);
        }

        void sendSms(String msg) {
            System.out.println("  SMS:   " + msg);
        }
    }

    // --- SOLUTION ---
    interface OrderObserver {
        void onUpdate(String message);
    }

    static class GoodOrder {
        private final String id;
        private String status;
        private final List<OrderObserver> observers = new ArrayList<>();

        GoodOrder(String id) {
            this.id = id;
            this.status = "CREATED";
        }

        void addObserver(OrderObserver o) {
            observers.add(o);
        }

        void setStatus(String status) {
            this.status = status;
            String msg = id + " is now " + status;
            for (OrderObserver o : observers) {
                o.onUpdate(msg);
            }
        }
    }
}

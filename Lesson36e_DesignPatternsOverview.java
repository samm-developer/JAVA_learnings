// Lesson 36e: Design Patterns — overview + more patterns (beyond 36b)
// Compile: javac Lesson36e_DesignPatternsOverview.java
// Run:     java Lesson36e_DesignPatternsOverview
//
// Study order:
//   36e  this file — full map (start here)
//   36f  Singleton       36g  Strategy       36h  Factory       36i  Builder
//   36j  Adapter         36k  Decorator      36l  Observer      36m  Template Method
//   36n  Facade
//   36d  SOLID principles
//   36b  legacy combined demo (Strategy + Factory + Builder in one file)
//
// ========== CHEAT SHEET ==========
//
// CREATIONAL (how objects are born)
//   Singleton   → one instance only (config, connection pool holder)
//   Factory     → central creation by type/name          (see 36b)
//   Builder     → step-by-step complex object            (see 36b)
//
// STRUCTURAL (how objects connect)
//   Adapter     → old API → new interface clients expect
//   Decorator   → add behavior without changing class
//   Facade      → one simple entry to many subsystems
//
// BEHAVIORAL (how objects behave together)
//   Strategy    → swap algorithm at runtime             (see 36b)
//   Observer    → notify listeners when state changes
//   Template    → fixed skeleton, subclasses fill steps
//
// Pattern = proven solution to a recurring design problem (not copy-paste boilerplate).

import java.util.ArrayList;
import java.util.List;

public class Lesson36e_DesignPatternsOverview {

    public static void main(String[] args) {
        whatIsPattern();
        singletonDemo();
        adapterDemo();
        decoratorDemo();
        observerDemo();
        templateMethodDemo();
        springMap();
        summary();
    }

    static void whatIsPattern() {
        System.out.println("=== 0) What is a design pattern? ===");
        System.out.println("""
                Real problem → many teams hit it → common solution gets a NAME.

                NOT:  "use Singleton everywhere"
                YES:  "I need exactly one config object → Singleton fits"

                Already in 36b: Strategy | Factory | Builder
                This file adds: Singleton | Adapter | Decorator | Observer | Template
                """);
    }

    // -------------------------------------------------------------------------
    // SINGLETON — one instance in the JVM
    // -------------------------------------------------------------------------
    static void singletonDemo() {
        System.out.println("=== 1) Singleton (Creational) ===");
        System.out.println("  Problem: many AppConfig objects → inconsistent settings");
        System.out.println("  Solution: private ctor + getInstance()");

        AppConfig a = AppConfig.getInstance();
        AppConfig b = AppConfig.getInstance();
        a.setTheme("dark");
        System.out.println("  same instance? " + (a == b));
        System.out.println("  b sees theme=" + b.getTheme() + " ✅");
        System.out.println();
    }

    static class AppConfig {
        private static final AppConfig INSTANCE = new AppConfig();
        private String theme = "light";

        private AppConfig() { } // nobody else can new AppConfig()

        static AppConfig getInstance() {
            return INSTANCE;
        }

        void setTheme(String theme) {
            this.theme = theme;
        }

        String getTheme() {
            return theme;
        }
    }

    // -------------------------------------------------------------------------
    // ADAPTER — make incompatible interface work
    // -------------------------------------------------------------------------
    static void adapterDemo() {
        System.out.println("=== 2) Adapter (Structural) ===");
        System.out.println("  Problem: app expects PaymentGateway.charge(), legacy has payLegacy()");
        System.out.println("  Solution: adapter wraps legacy behind expected interface");

        PaymentGateway gw = new LegacyPaymentAdapter(new LegacyPaySystem());
        System.out.println("  " + gw.charge(500));
        System.out.println();
    }

    interface PaymentGateway {
        String charge(int amount);
    }

    static class LegacyPaySystem {
        String payLegacy(int rupees) {
            return "Legacy paid " + rupees;
        }
    }

    static class LegacyPaymentAdapter implements PaymentGateway {
        private final LegacyPaySystem legacy;

        LegacyPaymentAdapter(LegacyPaySystem legacy) {
            this.legacy = legacy;
        }

        public String charge(int amount) {
            return legacy.payLegacy(amount); // translate new → old
        }
    }

    // -------------------------------------------------------------------------
    // DECORATOR — wrap object to add behavior
    // -------------------------------------------------------------------------
    static void decoratorDemo() {
        System.out.println("=== 3) Decorator (Structural) ===");
        System.out.println("  Problem: add logging to Coffee without editing Coffee class");
        System.out.println("  Solution: wrapper implements same interface, delegates + extra");

        Coffee plain = new SimpleCoffee();
        Coffee withMilk = new MilkDecorator(plain);
        Coffee fancy = new LoggingDecorator(withMilk);

        System.out.println("  " + fancy.describe() + " cost=" + fancy.cost());
        System.out.println();
    }

    interface Coffee {
        String describe();
        int cost();
    }

    static class SimpleCoffee implements Coffee {
        public String describe() {
            return "Coffee";
        }
        public int cost() {
            return 100;
        }
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

    // -------------------------------------------------------------------------
    // OBSERVER — publish / subscribe
    // -------------------------------------------------------------------------
    static void observerDemo() {
        System.out.println("=== 4) Observer (Behavioral) ===");
        System.out.println("  Problem: order status changes → email + SMS must know");
        System.out.println("  Solution: subject notifies registered observers");

        OrderSubject order = new OrderSubject("ORD-99");
        order.addObserver(msg -> System.out.println("  Email: " + msg));
        order.addObserver(msg -> System.out.println("  SMS:   " + msg));

        order.setStatus("SHIPPED");
        System.out.println();
    }

    interface OrderObserver {
        void onUpdate(String message);
    }

    static class OrderSubject {
        private final String id;
        private String status;
        private final List<OrderObserver> observers = new ArrayList<>();

        OrderSubject(String id) {
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

    // -------------------------------------------------------------------------
    // TEMPLATE METHOD — algorithm skeleton in base class
    // -------------------------------------------------------------------------
    static void templateMethodDemo() {
        System.out.println("=== 5) Template Method (Behavioral) ===");
        System.out.println("  Problem: PDF and HTML reports share steps but differ in export");
        System.out.println("  Solution: base class defines steps; subclasses override hooks");

        Report pdf = new PdfReport();
        Report html = new HtmlReport();
        pdf.generate();
        html.generate();
        System.out.println();
    }

    static abstract class Report {
        // template — fixed order
        final void generate() {
            fetchData();
            format();
            export();
        }

        void fetchData() {
            System.out.println("  [template] fetch data");
        }

        abstract void format();

        abstract void export();
    }

    static class PdfReport extends Report {
        void format() {
            System.out.println("  [pdf] format as PDF");
        }
        void export() {
            System.out.println("  [pdf] export file.pdf");
        }
    }

    static class HtmlReport extends Report {
        void format() {
            System.out.println("  [html] format as HTML");
        }
        void export() {
            System.out.println("  [html] export index.html");
        }
    }

    static void springMap() {
        System.out.println("=== 6) Patterns in Spring (you already use them) ===");
        System.out.println("""
                Factory     → BeanFactory creates beans by name/type
                Strategy    → different AuthProvider / PasswordEncoder impls
                Builder     → HttpClient.newBuilder(), Spring Security config builders
                Singleton   → default Spring bean scope (@Scope singleton)
                Observer    → ApplicationEventPublisher / @EventListener
                Template    → JdbcTemplate, RestTemplate (fixed flow, you plug callbacks)
                Proxy/Decorator → Spring AOP wraps methods (logging, security, tx)
                """);
    }

    static void summary() {
        System.out.println("=== Full map ===");
        System.out.println("""
                CREATIONAL     STRUCTURAL      BEHAVIORAL
                Singleton      Adapter         Strategy     (36b)
                Factory (36b)  Decorator       Observer
                Builder (36b)  Facade          Template Method

                One file per pattern (problem → solution in each):
                  36f Singleton   36g Strategy    36h Factory     36i Builder
                  36j Adapter     36k Decorator  36l Observer    36m Template Method
                  36n Facade

                Example: javac Lesson36f_Singleton.java && java Lesson36f_Singleton
                """);
    }
}

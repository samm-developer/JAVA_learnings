// Lesson 36f: Singleton pattern (Creational)
// Compile: javac Lesson36f_Singleton.java
// Run:     java Lesson36f_Singleton
//
// Study order:
//   36e  Design patterns overview
//   36f  Singleton          ← YOU ARE HERE
//   36g  Strategy
//   36h  Factory
//   36i  Builder
//   36j  Adapter
//   36k  Decorator
//   36l  Observer
//   36m  Template Method
//   36n  Facade
//
// ========== CHEAT SHEET ==========
// Problem:  many instances → inconsistent shared state (config, pool holder)
// Solution: private constructor + static getInstance() → one object for JVM
// Spring:   default @Scope is singleton (one bean per container)

public class Lesson36f_Singleton {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        System.out.println("=== PROBLEM: anyone can new AppConfig() ===");
        BadAppConfig a = new BadAppConfig();
        BadAppConfig b = new BadAppConfig();
        a.setTheme("dark");
        System.out.println("  a.theme=" + a.getTheme());
        System.out.println("  b.theme=" + b.getTheme() + "  ❌ different objects, different settings");
        System.out.println("  same instance? " + (a == b));
        System.out.println();
    }

    static void solution() {
        System.out.println("=== SOLUTION: Singleton (private ctor + getInstance) ===");
        AppConfig x = AppConfig.getInstance();
        AppConfig y = AppConfig.getInstance();
        x.setTheme("dark");
        System.out.println("  x.theme=" + x.getTheme());
        System.out.println("  y.theme=" + y.getTheme() + "  ✅ one object, shared state");
        System.out.println("  same instance? " + (x == y));
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: Singleton ===");
        System.out.println("""
                When:     exactly one instance must exist (app config, logger holder)
                How:      private constructor blocks `new`; getInstance() returns the one object
                Note:     instance fields (theme) are shared because references point to same object
                Next:     Lesson 36g Strategy
                """);
    }

    // --- PROBLEM: public constructor → many instances ---
    static class BadAppConfig {
        private String theme = "light";

        BadAppConfig() { } // anyone can call new BadAppConfig()

        void setTheme(String theme) {
            this.theme = theme;
        }

        String getTheme() {
            return theme;
        }
    }

    // --- SOLUTION: eager singleton ---
    static class AppConfig {
        private static final AppConfig INSTANCE = new AppConfig();
        private String theme = "light";

        private AppConfig() { }

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
}

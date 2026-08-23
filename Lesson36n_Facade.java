// Lesson 36n: Facade pattern (Structural)
// Compile: javac Lesson36n_Facade.java
// Run:     java Lesson36n_Facade
//
// After: Lesson 36m (Template Method)
//
// ========== CHEAT SHEET ==========
// Problem:  client must call TV, Amp, Player in correct order — knows too many subsystems
// Solution: Facade exposes one simple method (watchMovie) that orchestrates everything
// vs Adapter: facade simplifies many classes; adapter translates one incompatible class

public class Lesson36n_Facade {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        System.out.println("=== PROBLEM: client orchestrates many subsystems ===");
        Tv tv = new Tv();
        Amplifier amp = new Amplifier();
        DvdPlayer player = new DvdPlayer();

        tv.on();
        amp.on();
        amp.setVolume(5);
        player.on();
        player.play("Inception");
        System.out.println("  Client knows 3 classes + correct startup order ❌");
        System.out.println();
    }

    static void solution() {
        System.out.println("=== SOLUTION: Facade — one entry point ===");
        HomeTheaterFacade theater = new HomeTheaterFacade(new Tv(), new Amplifier(), new DvdPlayer());
        theater.watchMovie("Inception");
        System.out.println();
        theater.endMovie();
        System.out.println("  ✅ client calls theater.watchMovie() only");
        System.out.println();
    }

    static void summary() {
        System.out.println("=== Summary: Facade ===");
        System.out.println("""
                When:     many related classes must work together; client should stay simple
                How:      facade class holds subsystem refs, exposes high-level methods
                Done:     design pattern series 36f–36n complete
                Also see: 36e overview, 36d SOLID, 36b combined demo (legacy)
                """);
    }

    // --- subsystems (complex on their own) ---
    static class Tv {
        void on() { System.out.println("  TV on"); }
        void off() { System.out.println("  TV off"); }
    }

    static class Amplifier {
        void on() { System.out.println("  Amp on"); }
        void off() { System.out.println("  Amp off"); }
        void setVolume(int level) { System.out.println("  Amp volume=" + level); }
    }

    static class DvdPlayer {
        void on() { System.out.println("  DVD on"); }
        void off() { System.out.println("  DVD off"); }
        void play(String title) { System.out.println("  DVD playing: " + title); }
        void stop() { System.out.println("  DVD stop"); }
    }

    // --- SOLUTION: facade ---
    static class HomeTheaterFacade {
        private final Tv tv;
        private final Amplifier amp;
        private final DvdPlayer player;

        HomeTheaterFacade(Tv tv, Amplifier amp, DvdPlayer player) {
            this.tv = tv;
            this.amp = amp;
            this.player = player;
        }

        void watchMovie(String title) {
            tv.on();
            amp.on();
            amp.setVolume(5);
            player.on();
            player.play(title);
        }

        void endMovie() {
            player.stop();
            player.off();
            amp.off();
            tv.off();
        }
    }
}

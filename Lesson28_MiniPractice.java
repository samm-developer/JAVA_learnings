// Lesson 28 Mini Practice: write 3 movies, then read and print
// Compile: javac Lesson28_MiniPractice.java
// Run:     java Lesson28_MiniPractice

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Lesson28_MiniPractice {
    public static void main(String[] args) {
        Path file = Path.of("movies.txt");

        // ===== WRITE 3 movies (overwrite / create fresh) =====
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("Inception");
            writer.newLine();
            writer.write("Interstellar");
            writer.newLine();
            writer.write("The Dark Knight");
            writer.newLine();
            System.out.println("Wrote 3 movies to " + file);
        } catch (IOException e) {
            System.out.println("Write failed: " + e.getMessage());
        }

        // ===== READ and print =====
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            System.out.println("--- Movies ---");
            String line;
            int n = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(n + ". " + line);
                n++;
            }
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }

        // ===== Bonus: APPEND one more movie =====
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write("Oppenheimer");
            writer.newLine();
            System.out.println("Appended: Oppenheimer");
        } catch (IOException e) {
            System.out.println("Append failed: " + e.getMessage());
        }

        // Show final list
        try {
            System.out.println("--- Final list ---");
            int n = 1;
            for (String movie : Files.readAllLines(file)) {
                System.out.println(n + ". " + movie);
                n++;
            }
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }
    }
}

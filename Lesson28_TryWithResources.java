// Lesson 28: try-with-resources + modern file I/O
// Compile: javac Lesson28_TryWithResources.java
// Run:     java Lesson28_TryWithResources

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Lesson28_TryWithResources {
    public static void main(String[] args) {
        Path file = Path.of("lesson28_notes.txt");

        // ===== WRITE with try-with-resources =====
        // Writer is closed AUTOMATICALLY at the end of try
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("Line 1: Hello Java");
            writer.newLine();
            writer.write("Line 2: try-with-resources closes for you");
            writer.newLine();
            writer.write("Line 3: no writer.close() needed");
            writer.newLine();
            System.out.println("Wrote: " + file.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Write failed: " + e.getMessage());
        }

        // ===== READ line by line =====
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            System.out.println("--- reading line by line ---");
            String line;
            int n = 1;
            while ((line = reader.readLine()) != null) {
                System.out.println(n + ": " + line);
                n++;
            }
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }

        // ===== Modern one-liners (also fine for small files) =====
        try {
            System.out.println("--- Files.readAllLines ---");
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                System.out.println(line);
            }

            Files.writeString(Path.of("lesson28_hello.txt"), "Quick write!\n");
            String content = Files.readString(Path.of("lesson28_hello.txt"));
            System.out.println("readString: " + content.trim());
        } catch (IOException e) {
            System.out.println("Files helper failed: " + e.getMessage());
        }

        // Old style reminder (Lesson 15):
        // FileWriter w = new FileWriter(...);
        // ...
        // w.close();  // easy to forget, especially if an error happens mid-way
    }
}

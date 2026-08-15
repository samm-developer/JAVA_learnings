// Append lines to an existing file (don't overwrite)
// Compile: javac Lesson28b_AppendFile.java
// Run:     java Lesson28b_AppendFile

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Lesson28b_AppendFile {
    public static void main(String[] args) {
        Path file = Path.of("lesson28_notes.txt");

        // ===== APPEND with try-with-resources =====
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE,  // create file if missing
                StandardOpenOption.APPEND   // add at the END (keep old lines)
        )) {
            writer.write("Line 4: I appended this");
            writer.newLine();
            writer.write("Line 5: another new line");
            writer.newLine();
            System.out.println("Appended 2 lines to " + file);
        } catch (IOException e) {
            System.out.println("Append failed: " + e.getMessage());
        }

        // ===== Or append a whole string quickly =====
        try {
            Files.writeString(
                    file,
                    "Line 6: via Files.writeString\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.out.println("writeString append failed: " + e.getMessage());
        }

        // Show final file
        try {
            System.out.println("--- full file ---");
            List<String> lines = Files.readAllLines(file);
            for (int i = 0; i < lines.size(); i++) {
                System.out.println((i + 1) + ": " + lines.get(i));
            }
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }
    }
}

// Lesson 28c: NIO.2 Path & Files (modern file API)
// Compile: javac Lesson28c_NIO2.java
// Run:     java Lesson28c_NIO2
//
// Place: after Lesson 15 (File/Scanner) and Lesson 28 (try-with-resources).
// Prefer java.nio.file over old java.io.File for new code.

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Lesson28c_NIO2 {
    public static void main(String[] args) throws IOException {
        Path dir = Path.of("lesson28c-demo");
        Path file = dir.resolve("notes.txt");
        Path copy = dir.resolve("notes-copy.txt");

        // ===== 1) Create directory =====
        Files.createDirectories(dir);
        System.out.println("Dir: " + dir.toAbsolutePath());

        // ===== 2) Write text (overwrite) =====
        Files.writeString(file, "Line 1\nLine 2\n", StandardCharsets.UTF_8);
        System.out.println("Wrote: " + file);

        // ===== 3) Append =====
        Files.writeString(
                file,
                "Appended line\n",
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND
        );

        // ===== 4) Read all lines =====
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        System.out.println("--- contents ---");
        lines.forEach(System.out::println);

        // ===== 5) Copy / exists / size =====
        Files.copy(file, copy, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copy exists? " + Files.exists(copy));
        System.out.println("Size bytes: " + Files.size(file));

        // ===== 6) Walk directory (list files) =====
        System.out.println("--- files in dir ---");
        try (var stream = Files.list(dir)) {
            stream.forEach(p -> System.out.println("  " + p.getFileName()));
        }

        // ===== 7) Cleanup demo files =====
        Files.deleteIfExists(copy);
        Files.deleteIfExists(file);
        Files.deleteIfExists(dir);
        System.out.println("Cleaned up demo folder");
    }
}

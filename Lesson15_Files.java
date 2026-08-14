// Lesson 15: Files (read & write text)
// Compile: javac Lesson15_Files.java
// Run:     java Lesson15_Files

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Lesson15_Files {
    public static void main(String[] args) {
        String fileName = "notes.txt";

        // ===== WRITE a file =====
        // true  = append to existing file
        // false = overwrite (create new / replace)
        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write("Hello from Java!\n");
            writer.write("Learning file I/O.\n");
            writer.write("Line 3: practice makes progress.\n");
            writer.close(); // always close when done writing
            System.out.println("Wrote to " + fileName);
        } catch (IOException e) {
            System.out.println("Write failed: " + e.getMessage());
        }

        // ===== READ a file (line by line) =====
        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);

            System.out.println("--- file contents ---");
            int lineNo = 1;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                System.out.println(lineNo + ": " + line);
                lineNo++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }

        // ===== File info =====
        File file = new File(fileName);
        System.out.println("--- file info ---");
        System.out.println("Exists? " + file.exists());
        System.out.println("Name: " + file.getName());
        System.out.println("Path: " + file.getAbsolutePath());
        System.out.println("Size (bytes): " + file.length());

        // ===== APPEND more text =====
        try {
            FileWriter writer = new FileWriter(fileName, true); // append mode
            writer.write("Appended line.\n");
            writer.close();
            System.out.println("Appended one more line.");
        } catch (IOException e) {
            System.out.println("Append failed: " + e.getMessage());
        }

        // Show final contents
        try {
            Scanner reader = new Scanner(new File(fileName));
            System.out.println("--- after append ---");
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
        }

        // Optional: delete the file when you're done experimenting
        // file.delete();
    }
}

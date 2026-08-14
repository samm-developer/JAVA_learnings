// Lesson 16: Mini-project — Student Grade Book
// Combines: classes, ArrayList, Scanner, files, loops, if/else, methods
//
// Compile: javac Lesson16_MiniProject.java
// Run:     java Lesson16_MiniProject
//
// Menu:
//  1) Add student
//  2) List students
//  3) Find student
//  4) Class average
//  5) Save to file
//  6) Load from file
//  0) Exit

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class StudentRecord {
    private String name;
    private int marks;

    StudentRecord(String name, int marks) {
        this.name = name;
        setMarks(marks);
    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        if (marks < 0) {
            marks = 0;
        }
        if (marks > 100) {
            marks = 100;
        }
        this.marks = marks;
    }

    public String grade() {
        if (marks >= 90) return "A";
        if (marks >= 75) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    // One line for saving to file: name,marks
    public String toFileLine() {
        return name + "," + marks;
    }

    public static StudentRecord fromFileLine(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        int marks = Integer.parseInt(parts[1]);
        return new StudentRecord(name, marks);
    }

    @Override
    public String toString() {
        return name + " | marks=" + marks + " | grade=" + grade();
    }
}

public class Lesson16_MiniProject {
    private static final String FILE_NAME = "students.csv";
    private static ArrayList<StudentRecord> students = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Student Grade Book ===");

        while (running) {
            printMenu();
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear leftover Enter after nextInt()

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> listStudents();
                case 3 -> findStudent(sc);
                case 4 -> classAverage();
                case 5 -> saveToFile();
                case 6 -> loadFromFile();
                case 0 -> {
                    System.out.println("Bye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try 0-6.");
            }
        }

        sc.close();
    }

    static void printMenu() {
        System.out.println();
        System.out.println("1. Add student");
        System.out.println("2. List students");
        System.out.println("3. Find student");
        System.out.println("4. Class average");
        System.out.println("5. Save to file");
        System.out.println("6. Load from file");
        System.out.println("0. Exit");
    }

    static void addStudent(Scanner sc) {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Marks (0-100): ");
        int marks = sc.nextInt();
        sc.nextLine();

        students.add(new StudentRecord(name, marks));
        System.out.println("Added.");
    }

    static void listStudents() {
        if (students.isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        System.out.println("--- Students ---");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
    }

    static void findStudent(Scanner sc) {
        System.out.print("Search name: ");
        String query = sc.nextLine();
        boolean found = false;

        for (StudentRecord s : students) {
            if (s.getName().equalsIgnoreCase(query)) {
                System.out.println("Found: " + s);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No student named '" + query + "'.");
        }
    }

    static void classAverage() {
        if (students.isEmpty()) {
            System.out.println("No students yet.");
            return;
        }
        int sum = 0;
        for (StudentRecord s : students) {
            sum += s.getMarks();
        }
        double avg = (double) sum / students.size();
        System.out.println("Class average: " + avg);
    }

    static void saveToFile() {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, false);
            for (StudentRecord s : students) {
                writer.write(s.toFileLine() + "\n");
            }
            writer.close();
            System.out.println("Saved " + students.size() + " student(s) to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No file found: " + FILE_NAME);
            return;
        }

        try {
            Scanner reader = new Scanner(file);
            students.clear();
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (line.isEmpty()) continue;
                students.add(StudentRecord.fromFileLine(line));
            }
            reader.close();
            System.out.println("Loaded " + students.size() + " student(s) from " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Load failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Bad file format: " + e.getMessage());
        }
    }
}

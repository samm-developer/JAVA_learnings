// Lesson 32: Capstone — Personal Task List
// Combines: classes, ArrayList, LocalDate, files, sorting, Optional-style checks
//
// Compile: javac Lesson32_TaskList.java
// Run:     java Lesson32_TaskList

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

record Task(String title, LocalDate due, boolean done) {
    String toFileLine() {
        return title.replace(",", " ") + "," + due + "," + done;
    }

    static Task fromFileLine(String line) {
        String[] p = line.split(",", 3);
        return new Task(p[0], LocalDate.parse(p[1]), Boolean.parseBoolean(p[2]));
    }
}

public class Lesson32_TaskList {
    private static final Path FILE = Path.of("tasks.csv");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        load();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Personal Task List ===");

        while (running) {
            printMenu();
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear leftover Enter

            switch (choice) {
                case 1 -> addTask(sc);
                case 2 -> listTasks();
                case 3 -> markDone(sc);
                case 4 -> listOverdue();
                case 5 -> deleteTask(sc);
                case 6 -> editDueDate(sc);
                case 7 -> save();
                case 0 -> {
                    save();
                    System.out.println("Saved. Bye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    static void printMenu() {
        System.out.println();
        System.out.println("1. Add task");
        System.out.println("2. List tasks (by due date)");
        System.out.println("3. Mark task done");
        System.out.println("4. Show overdue");
        System.out.println("5. Delete task");
        System.out.println("6. Edit due date");
        System.out.println("7. Save");
        System.out.println("0. Exit");
    }

    static void addTask(Scanner sc) {
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Due in how many days from today? ");
        int days = sc.nextInt();
        sc.nextLine();

        LocalDate due = LocalDate.now().plusDays(days);
        tasks.add(new Task(title, due, false));
        System.out.println("Added. Due: " + due.format(FMT));
    }

    static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }
        tasks.sort(Comparator.comparing(Task::due));
        System.out.println("--- Tasks ---");
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            String status = t.done() ? "DONE" : "TODO";
            System.out.println((i + 1) + ". [" + status + "] " + t.title()
                    + " | due " + t.due().format(FMT));
        }
    }

    static void markDone(Scanner sc) {
        listTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Task number to mark done: ");
        int n = sc.nextInt();
        sc.nextLine();

        if (n < 1 || n > tasks.size()) {
            System.out.println("Invalid number.");
            return;
        }

        Task old = tasks.get(n - 1);
        tasks.set(n - 1, new Task(old.title(), old.due(), true));
        System.out.println("Marked done: " + old.title());
    }

    static void deleteTask(Scanner sc) {
        listTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Task number to delete: ");
        int n = sc.nextInt();
        sc.nextLine();

        if (n < 1 || n > tasks.size()) {
            System.out.println("Invalid number.");
            return;
        }

        Task removed = tasks.remove(n - 1);
        System.out.println("Deleted: " + removed.title());
    }

    static void editDueDate(Scanner sc) {
        listTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Task number to edit: ");
        int n = sc.nextInt();
        sc.nextLine();

        if (n < 1 || n > tasks.size()) {
            System.out.println("Invalid number.");
            return;
        }

        System.out.print("New due date — days from today (0 = today): ");
        int days = sc.nextInt();
        sc.nextLine();

        Task old = tasks.get(n - 1);
        LocalDate newDue = LocalDate.now().plusDays(days);
        tasks.set(n - 1, new Task(old.title(), newDue, old.done()));
        System.out.println("Updated \"" + old.title() + "\" due date → " + newDue.format(FMT));
    }

    static void listOverdue() {
        LocalDate today = LocalDate.now();
        List<Task> overdue = tasks.stream()
                .filter(t -> !t.done() && t.due().isBefore(today))
                .sorted(Comparator.comparing(Task::due))
                .toList();

        if (overdue.isEmpty()) {
            System.out.println("No overdue tasks.");
            return;
        }

        System.out.println("--- Overdue ---");
        for (Task t : overdue) {
            long days = ChronoUnit.DAYS.between(t.due(), today);
            System.out.println(t.title() + " | " + days + " day(s) late");
        }
    }

    static void save() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE)) {
            for (Task t : tasks) {
                w.write(t.toFileLine());
                w.newLine();
            }
            System.out.println("Saved " + tasks.size() + " task(s) to " + FILE);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    static void load() {
        if (!Files.exists(FILE)) return;
        try {
            tasks.clear();
            for (String line : Files.readAllLines(FILE)) {
                if (line.isBlank()) continue;
                tasks.add(Task.fromFileLine(line));
            }
            System.out.println("Loaded " + tasks.size() + " task(s).");
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }

    // Optional helper example (not used in menu, for learning)
    static Optional<Task> findByTitle(String title) {
        return tasks.stream()
                .filter(t -> t.title().equalsIgnoreCase(title))
                .findFirst();
    }
}

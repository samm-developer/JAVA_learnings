// Lesson 14: Scanner (read input from the keyboard)
// Compile: javac Lesson14_Scanner.java
// Run:     java Lesson14_Scanner
// Then type values when asked. Ctrl+D (Mac/Linux) or Ctrl+Z (Windows) is not needed —
// the program ends after the last question.

import java.util.Scanner;

public class Lesson14_Scanner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine(); // reads a full line (including spaces)

        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        System.out.print("Enter your height in feet (e.g. 5.9): ");
        double height = sc.nextDouble();

        // IMPORTANT: after nextInt()/nextDouble(), leftover newline stays in the buffer.
        // Call nextLine() once to consume it before reading another line of text.
        sc.nextLine();

        System.out.print("Enter your city: ");
        String city = sc.nextLine();

        System.out.println("---");
        System.out.println("Hello, " + name + "!");
        System.out.println("Age: " + age);
        System.out.println("Height: " + height);
        System.out.println("City: " + city);

        if (age >= 18) {
            System.out.println("You are an adult.");
        } else {
            System.out.println("You are a minor.");
        }

        sc.close();
    }
}

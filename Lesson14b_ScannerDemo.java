// Lesson 14b: Scanner without typing (reads from a String)
// Useful to see the same APIs without waiting for keyboard input.
// Compile: javac Lesson14b_ScannerDemo.java
// Run:     java Lesson14b_ScannerDemo

import java.util.Scanner;

public class Lesson14b_ScannerDemo {
    public static void main(String[] args) {
        String fakeInput = "Shashwat\n22\n5.9\nBengaluru\n";
        Scanner sc = new Scanner(fakeInput);

        String name = sc.nextLine();
        int age = sc.nextInt();
        double height = sc.nextDouble();
        sc.nextLine(); // clear leftover newline
        String city = sc.nextLine();

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Height: " + height);
        System.out.println("City: " + city);

        // next() reads one word only (stops at space)
        Scanner words = new Scanner("Java is fun");
        System.out.println("next(): " + words.next());      // Java
        System.out.println("next(): " + words.next());      // is
        System.out.println("nextLine(): " + words.nextLine().trim()); // fun

        words.close();
        sc.close();
    }
}

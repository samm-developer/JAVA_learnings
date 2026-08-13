// Lesson 6: Methods (reusable blocks of code)
// Compile: javac Lesson06_Methods.java
// Run:     java Lesson06_Methods

public class Lesson06_Methods {

    public static void main(String[] args) {
        // Call methods from main
        sayHello();
        sayHelloTo("Shashwat");

        int sum = add(10, 20);
        System.out.println("Sum = " + sum);

        System.out.println("Max of 8 and 15 = " + max(8, 15));
        System.out.println("Is 7 even? " + isEven(7));
        System.out.println("Area of circle r=5 = " + circleArea(5));

        // Method overloading: same name, different params
        System.out.println("add(2, 3) = " + add(2, 3));
        System.out.println("add(2, 3, 4) = " + add(2, 3, 4));
        System.out.println("add(2.5, 3.5) = " + add(2.5, 3.5));

        printLine(20);
        greet("Java", 3);
    }

    // ===== No parameters, no return (void) =====
    public static void sayHello() {
        System.out.println("Hello from a method!");
    }

    // ===== With parameter, no return =====
    public static void sayHelloTo(String name) {
        System.out.println("Hello, " + name + "!");
    }

    // ===== With parameters AND return value =====
    // return type is int
    public static int add(int a, int b) {
        return a + b; // sends value back to caller
    }

    // Overload: 3 ints
    public static int add(int a, int b, int c) {
        return a + b + c;
    }

    // Overload: doubles
    public static double add(double a, double b) {
        return a + b;
    }

    public static int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    public static double circleArea(double radius) {
        return 3.14159 * radius * radius;
    }

    public static void printLine(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // Multiple parameters
    public static void greet(String word, int times) {
        for (int i = 1; i <= times; i++) {
            System.out.println(i + ": " + word);
        }
    }
}

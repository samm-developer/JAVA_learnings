// Lesson 12: Exceptions (handle errors gracefully)
// Compile: javac Lesson12_Exceptions.java
// Run:     java Lesson12_Exceptions
// Or:      java Lesson12_Exceptions 10

public class Lesson12_Exceptions {
    public static void main(String[] args) {

        // ===== Without handling: program crashes =====
        // int x = 10 / 0; // ArithmeticException

        // ===== try / catch: handle the error =====
        System.out.println("--- try / catch ---");
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Program continues after catch.");

        // ===== Multiple catch blocks =====
        System.out.println("--- multiple catch ---");
        try {
            String text = null;
            System.out.println(text.length()); // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("Text was null!");
        } catch (Exception e) {
            System.out.println("Some other error: " + e);
        }

        // ===== finally: always runs =====
        System.out.println("--- finally ---");
        try {
            int[] nums = {1, 2, 3};
            System.out.println(nums[5]); // ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid index!");
        } finally {
            System.out.println("finally block always runs.");
        }

        // ===== Parsing user input safely =====
        System.out.println("--- parse input ---");
        String input = (args.length > 0) ? args[0] : "abc";
        try {
            int age = Integer.parseInt(input);
            System.out.println("Age is " + age);
        } catch (NumberFormatException e) {
            System.out.println("'" + input + "' is not a valid number.");
        }

        // ===== throw your own exception =====
        System.out.println("--- throw ---");
        try {
            withdraw(500, 200);
            withdraw(500, 800); // not enough balance
        } catch (IllegalArgumentException e) {
            System.out.println("Withdraw failed: " + e.getMessage());
        }

        // ===== Custom exception =====
        System.out.println("--- custom exception ---");
        try {
            checkAge(15);
        } catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }

    public static void withdraw(double balance, double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        System.out.println("Withdrew " + amount + ". Remaining: " + (balance - amount));
    }

    public static void checkAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Age must be 18 or above.");
        }
        System.out.println("Age verified: " + age);
    }
}

// Custom checked exception
class InvalidAgeException extends Exception {
    InvalidAgeException(String message) {
        super(message);
    }
}

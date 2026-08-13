// Lesson 2: Variables & Data Types
// Compile: javac Lesson02_Variables.java
// Run:     java Lesson02_Variables

public class Lesson02_Variables {
    public static void main(String[] args) {

        // ===== Primitive types (store the value itself) =====
        byte smallNumber = 100;          // -128 to 127
        short mediumNumber = 32000;      // ~ -32K to 32K
        int age = 25;                    // most common whole number
        long bigNumber = 9_000_000_000L; // need L for long literals

        float price = 99.99f;            // need f for float
        double height = 5.9;             // preferred for decimals

        char grade = 'A';                // single character, single quotes
        boolean isStudent = true;        // true or false

        // ===== Reference type (stores a reference to an object) =====
        String name = "Shashwat";        // text, double quotes

        // ===== Print them =====
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Height: " + height);
        System.out.println("Grade: " + grade);
        System.out.println("Is student? " + isStudent);
        System.out.println("Price: " + price);
        System.out.println("Small: " + smallNumber);
        System.out.println("Medium: " + mediumNumber);
        System.out.println("Big: " + bigNumber);

        // ===== Updating a variable =====
        age = 26; // same type, new value
        System.out.println("Updated age: " + age);

        // ===== final = cannot change later =====
        final double PI = 3.14159;
        System.out.println("PI: " + PI);
        // PI = 3.14; // ERROR if you uncomment this

        // ===== Type casting =====
        int whole = (int) 9.8; // 9 (fraction dropped)
        double fromInt = age;  // widening: int → double (automatic)
        System.out.println("Casted: " + whole);
        System.out.println("From int: " + fromInt);
    }
}

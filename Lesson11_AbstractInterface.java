// Lesson 11: Abstract classes & Interfaces
// Compile: javac Lesson11_AbstractInterface.java
// Run:     java Lesson11_AbstractInterface

// ===== Abstract class =====
// - Can have abstract methods (no body) + normal methods
// - Cannot do: new Shape(...)
// - Subclass MUST implement abstract methods
abstract class Shape {
    String name;

    Shape(String name) {
        this.name = name;
    }

    // Must be implemented by children
    abstract double area();

    // Shared normal method
    void display() {
        System.out.println(name + " area = " + area());
    }
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }

    @Override
    double area() {
        return 3.14159 * radius * radius;
    }
}

class Rectangle extends Shape {
    double width;
    double height;

    Rectangle(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }

    @Override
    double area() {
        return width * height;
    }
}

// ===== Interface =====
// - Pure contract: what a class CAN do
// - Methods are public abstract by default (before Java default methods)
// - A class can implement MANY interfaces (but extend only ONE class)
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

class Bird implements Flyable {
    String name;

    Bird(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println(name + " is flying.");
    }
}

class Duck implements Flyable, Swimmable {
    String name;

    Duck(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println(name + " flies short distances.");
    }

    @Override
    public void swim() {
        System.out.println(name + " is swimming.");
    }
}

public class Lesson11_AbstractInterface {
    public static void main(String[] args) {
        // Shape s = new Shape("x"); // ERROR: abstract cannot be instantiated

        Shape c = new Circle(5);
        Shape r = new Rectangle(4, 6);
        c.display();
        r.display();

        System.out.println("---");

        Bird parrot = new Bird("Parrot");
        Duck donald = new Duck("Donald");

        parrot.fly();
        donald.fly();
        donald.swim();

        // Interface as a type (polymorphism)
        Flyable f1 = parrot;
        Flyable f2 = donald;
        f1.fly();
        f2.fly();
    }
}

// Lesson 8: Classes & Objects (OOP basics)
// Compile: javac Lesson08_ClassesObjects.java
// Run:     java Lesson08_ClassesObjects

// A class is a blueprint. An object is a real instance built from it.
class Student {
    // ===== Fields (data / state) =====
    String name;
    int age;
    double marks;

    // ===== Constructor: runs when you do `new Student(...)` =====
    Student(String name, int age, double marks) {
        this.name = name;   // this.name = field, name = parameter
        this.age = age;
        this.marks = marks;
    }

    // ===== Methods (behavior) =====
    void introduce() {
        System.out.println("Hi, I'm " + name + ", age " + age + ".");
    }

    boolean hasPassed() {
        return marks >= 40;
    }

    void study(int hours) {
        marks += hours * 0.5; // studying improves marks a bit
        if (marks > 100) {
            marks = 100;
        }
        System.out.println(name + " studied " + hours + "h. Marks now: " + marks);
    }
}

public class Lesson08_ClassesObjects {
    public static void main(String[] args) {

        // Create objects (instances)
        Student s1 = new Student("Shashwat", 22, 78);
        Student s2 = new Student("Asha", 21, 35);

        // Each object has its own data
        s1.introduce();
        s2.introduce();

        System.out.println(s1.name + " passed? " + s1.hasPassed());
        System.out.println(s2.name + " passed? " + s2.hasPassed());

        s2.study(20);
        System.out.println(s2.name + " passed? " + s2.hasPassed());

        // You can also read/update fields directly (for now)
        System.out.println("s1 marks: " + s1.marks);
        s1.marks = 95;
        System.out.println("s1 marks updated: " + s1.marks);

        // Arrays of objects
        Student[] classRoom = {s1, s2, new Student("Ravi", 23, 88)};
        System.out.println("--- Classroom ---");
        for (Student s : classRoom) {
            s.introduce();
        }
    }
}

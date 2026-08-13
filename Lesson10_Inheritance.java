// Lesson 10: Inheritance (reuse + extend a class)
// Compile: javac Lesson10_Inheritance.java
// Run:     java Lesson10_Inheritance

// Parent / Superclass
class Animal {
    protected String name; // protected = visible to subclasses

    Animal(String name) {
        this.name = name;
    }

    void eat() {
        System.out.println(name + " is eating.");
    }

    void sleep() {
        System.out.println(name + " is sleeping.");
    }

    void speak() {
        System.out.println(name + " makes a sound.");
    }
}

// Child / Subclass — "is-a" Animal
class Dog extends Animal {
    String breed;

    Dog(String name, String breed) {
        super(name); // call parent constructor FIRST
        this.breed = breed;
    }

    // Extra behavior only dogs have
    void fetch() {
        System.out.println(name + " fetches the ball!");
    }

    // Override: replace parent version
    @Override
    void speak() {
        System.out.println(name + " says: Woof!");
    }
}

class Cat extends Animal {
    Cat(String name) {
        super(name);
    }

    @Override
    void speak() {
        System.out.println(name + " says: Meow!");
    }
}

public class Lesson10_Inheritance {
    public static void main(String[] args) {
        Dog dog = new Dog("Bruno", "Labrador");
        Cat cat = new Cat("Milo");

        // Inherited methods
        dog.eat();
        dog.sleep();
        dog.fetch();      // Dog-only
        dog.speak();      // overridden

        System.out.println("---");

        cat.eat();
        cat.speak();      // overridden differently

        System.out.println("---");

        // Polymorphism: parent reference, child object
        Animal a1 = new Dog("Rex", "Beagle");
        Animal a2 = new Cat("Luna");

        a1.speak(); // Dog's speak
        a2.speak(); // Cat's speak

        // Useful: treat many animals the same way
        Animal[] zoo = { dog, cat, a1, a2 };
        System.out.println("--- zoo speak ---");
        for (Animal animal : zoo) {
            animal.speak();
        }
    }
}

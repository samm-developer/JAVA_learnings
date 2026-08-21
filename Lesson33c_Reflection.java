// Lesson 33c: Reflection (light intro)
// Compile: javac Lesson33c_Reflection.java
// Run:     java Lesson33c_Reflection
//
// Place: right after custom annotations (33b).
// Reflection = inspect / call code at runtime by name.
// This file shows NORMAL code vs REFLECTION side by side.

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Person33c {
    private String name;
    private int age;

    public Person33c() {
        this("unknown", 0);
    }

    public Person33c(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    // NORMAL way to change private field: public setter
    public void setName(String name) {
        this.name = name;
    }

    public String greet() {
        return "Hi, I am " + name + " (" + age + ")";
    }

    private String secret() {
        return "private ok";
    }
}

public class Lesson33c_Reflection {
    public static void main(String[] args) throws Exception {

        // =====================================================================
        // A) CREATE OBJECT
        // =====================================================================
        System.out.println("=== A) Create object ===");

        // NORMAL
        Person33c normalPerson = new Person33c("Asha", 21);
        System.out.println("NORMAL:      " + normalPerson.greet());

        // REFLECTION (same result)
        Class<?> clazz = Person33c.class;
        Constructor<?> ctor = clazz.getConstructor(String.class, int.class);
        Object reflectedPerson = ctor.newInstance("Asha", 21);
        Method greet = clazz.getMethod("greet");
        System.out.println("REFLECTION:  " + greet.invoke(reflectedPerson));

        // =====================================================================
        // B) CALL PUBLIC METHOD
        // =====================================================================
        System.out.println();
        System.out.println("=== B) Call public method greet() ===");

        // NORMAL
        String normalGreet = normalPerson.greet();
        System.out.println("NORMAL:      " + normalGreet);

        // REFLECTION
        // getMethod("greet") = find method by name (String)
        // invoke(obj)        = run it on that object
        String reflectedGreet = (String) greet.invoke(reflectedPerson);
        System.out.println("REFLECTION:  " + reflectedGreet);

        // =====================================================================
        // C) CHANGE name
        // =====================================================================
        System.out.println();
        System.out.println("=== C) Change name ===");

        // NORMAL — use setter (preferred in real apps)
        // cannot do: normalPerson.name = "Riya";  (private field)
        normalPerson.setName("Riya");
        System.out.println("NORMAL setter: " + normalPerson.greet());

        // REFLECTION — touch private field directly (teaching only; avoid in real apps)
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true); // unlock private
        nameField.set(reflectedPerson, "Riya");
        System.out.println("REFLECTION:    " + greet.invoke(reflectedPerson));

        // =====================================================================
        // D) CALL PRIVATE METHOD
        // =====================================================================
        System.out.println();
        System.out.println("=== D) Call private method secret() ===");

        // NORMAL — cannot do: normalPerson.secret();  (private)

        // REFLECTION
        Method secret = clazz.getDeclaredMethod("secret");
        secret.setAccessible(true);
        System.out.println("REFLECTION:  " + secret.invoke(reflectedPerson));

        // =====================================================================
        // E) LIST METHODS (reflection-only feature)
        // =====================================================================
        System.out.println();
        System.out.println("=== E) List all declared methods (reflection) ===");
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println("  " + m.getName());
        }

        System.out.println();
        System.out.println("Summary:");
        System.out.println("  NORMAL      = write method/field names in source code");
        System.out.println("  REFLECTION  = find & call them by String name at runtime");
        System.out.println("  Spring uses reflection to read @Service and create beans.");
    }
}

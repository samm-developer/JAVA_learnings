// Lesson 33b: Custom annotations
// Compile: javac Lesson33b_CustomAnnotations.java
// Run:     java Lesson33b_CustomAnnotations
//
// Place: after enums/records; before (or with) understanding @Service / @Override.
// Annotations = metadata attached to code. Frameworks read them (often via reflection).

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

// ===== Define a custom annotation =====
@Retention(RetentionPolicy.RUNTIME) // keep it available at runtime
@Target(ElementType.METHOD)         // can only put on methods
@interface TestCase {
    String id();

    boolean enabled() default true;
}

class Calculator {
    @TestCase(id = "ADD-01")
    int add(int a, int b) {
        return a + b;
    }

    @TestCase(id = "MUL-01", enabled = false)
    int multiply(int a, int b) {
        return a * b;
    }

    int subtract(int a, int b) {
        return a - b; // no annotation
    }
}

public class Lesson33b_CustomAnnotations {
    public static void main(String[] args) throws Exception {
        Calculator calc = new Calculator();

        for (Method method : Calculator.class.getDeclaredMethods()) {
            TestCase ann = method.getAnnotation(TestCase.class);
            if (ann == null) {
                System.out.println(method.getName() + " → no @TestCase");
                continue;
            }
            System.out.println(method.getName()
                    + " → id=" + ann.id()
                    + ", enabled=" + ann.enabled());

            if (ann.enabled()) {
                Object result = method.invoke(calc, 3, 4);
                System.out.println("   ran → result=" + result);
            } else {
                System.out.println("   skipped");
            }
        }

        System.out.println();
        System.out.println("Spring's @Service / @RestController work the same idea:");
        System.out.println("  annotation on class → framework scans & wires beans.");
    }
}

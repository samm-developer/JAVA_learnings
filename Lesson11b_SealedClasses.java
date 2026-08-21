// Lesson 11b: Sealed classes (Java 17+) — after abstract/interfaces
// Compile: javac Lesson11b_SealedClasses.java
// Run:     java Lesson11b_SealedClasses
//
// Why: abstract/interface allow ANY subclass.
// Sealed = you LIST the only allowed subtypes (safer hierarchies).

// Only Circle, Rectangle, Triangle may extend Shape
sealed abstract class Shape11b permits Circle11b, Rectangle11b, Triangle11b {
    abstract double area();
}

final class Circle11b extends Shape11b {
    final double r;

    Circle11b(double r) {
        this.r = r;
    }

    @Override
    double area() {
        return Math.PI * r * r;
    }
}

final class Rectangle11b extends Shape11b {
    final double w, h;

    Rectangle11b(double w, double h) {
        this.w = w;
        this.h = h;
    }

    @Override
    double area() {
        return w * h;
    }
}

// non-sealed = this branch can be extended further by anyone
non-sealed class Triangle11b extends Shape11b {
    final double base, height;

    Triangle11b(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    double area() {
        return 0.5 * base * height;
    }
}

// Extra subtype under the non-sealed branch is OK
final class RightTriangle11b extends Triangle11b {
    RightTriangle11b(double a, double b) {
        super(a, b);
    }
}

public class Lesson11b_SealedClasses {
    public static void main(String[] args) {
        Shape11b[] shapes = {
                new Circle11b(2),
                new Rectangle11b(3, 4),
                new Triangle11b(6, 4),
                new RightTriangle11b(3, 4)
        };

        for (Shape11b s : shapes) {
            // Exhaustive switch: compiler knows all sealed subtypes
            String kind = switch (s) {
                case Circle11b c -> "Circle r=" + c.r;
                case Rectangle11b r -> "Rectangle " + r.w + "x" + r.h;
                case Triangle11b t -> "Triangle base=" + t.base;
                // no default needed if every permitted type is covered
            };
            System.out.println(kind + " area=" + String.format("%.2f", s.area()));
        }

        System.out.println();
        System.out.println("Rules:");
        System.out.println("  sealed   → only listed subtypes");
        System.out.println("  final    → no further subclass");
        System.out.println("  non-sealed → open again for extension");
    }
}
